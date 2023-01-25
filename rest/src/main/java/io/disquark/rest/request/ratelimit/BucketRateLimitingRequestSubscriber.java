package io.disquark.rest.request.ratelimit;

import static io.disquark.rest.request.HttpClientRequester.FALLBACK_REQUEST_ID;
import static io.disquark.rest.request.HttpClientRequester.REQUEST_ID;
import static io.disquark.rest.util.ExceptionPredicate.is;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Flow;

import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requester;
import io.disquark.rest.response.HttpResponse;
import io.disquark.rest.response.RateLimitException;
import io.smallrye.mutiny.Context;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.MultiSubscriber;
import io.vertx.mutiny.core.Promise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BucketRateLimitingRequestSubscriber implements MultiSubscriber<CompletableRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(BucketRateLimitingRequestSubscriber.class);
    private static final String ITERATION = "iteration";
    private final BucketCacheKey bucketKey;
    private final Requester<HttpResponse> requester;

    private volatile long resetAfter;

    private Flow.Subscription subscription;

    public BucketRateLimitingRequestSubscriber(BucketCacheKey bucketKey, Requester<HttpResponse> requester) {
        this.bucketKey = bucketKey;
        this.requester = requester;
    }

    private int getIteration(Context ctx) {
        return ctx.getOrElse(ITERATION, () -> 0);
    }

    private Uni<HttpResponse> request(Request request, Context ctx) {
        return requester.request(request)
                .onFailure(is(RateLimitException.class).and(x -> getIteration(ctx) < 5)).retry().when(multi -> {
                    return multi.onItem().castTo(RateLimitException.class)
                            .onItem().transformToUniAndMerge(rateLimit -> {
                                Duration retryAfter = Duration.between(Instant.now(),
                                        Instant.now().plusSeconds(Math.round(rateLimit.getResponse().retryAfter())));

                                if (!retryAfter.isZero() && !retryAfter.isNegative()) {
                                    return Uni.createFrom().item(rateLimit).onItem().delayIt().by(retryAfter);
                                }
                                return Uni.createFrom().item(rateLimit);
                            })
                            .invoke(() -> {
                                ctx.put(ITERATION, getIteration(ctx) + 1);
                                LOG.debug("Retrying outgoing request {} after encountering rate limit error, attempts: {}",
                                        ctx.getOrElse(REQUEST_ID, FALLBACK_REQUEST_ID), ctx.get(ITERATION));
                            });
                });
    }

    private Duration getResetAfterDuration() {
        return Duration.between(Instant.now(), Instant.now().plusSeconds(resetAfter));
    }

    private void request() {
        LOG.debug("Signalling demand for next request for buckets matching key {}", bucketKey);
        subscription.request(1);
    }

    @Override
    public void onItem(CompletableRequest item) {
        Promise<HttpResponse> promise = item.getResponsePromise();

        Uni.createFrom().context(ctx -> request(item.getRequest(), ctx))
                .call(response -> {
                    String bucket = response.getHeader("X-RateLimit-Bucket");
                    if (bucket != null) {
                        if (bucketKey.getTopLevelResourceValue().isPresent()) {
                            bucket += '-' + bucketKey.getTopLevelResourceValue().get();
                        }
                        return Uni.createFrom().item(bucket).invoke(item.getBucketConsumer());
                    }

                    LOG.debug("Request matching bucket key {} didn't return a bucket value", bucketKey);
                    return Uni.createFrom().voidItem();
                })
                .call(response -> {
                    String remaining = response.getHeader("X-RateLimit-Remaining");
                    if (remaining != null && Integer.parseInt(remaining) == 0) {
                        String resetAfter = response.getHeader("X-RateLimit-Reset-After");
                        if (resetAfter != null) {
                            LOG.debug("No requests remaining for buckets matching key {} for the next {}s",
                                    bucketKey, resetAfter);

                            return Uni.createFrom().voidItem()
                                    .invoke(() -> this.resetAfter = Math.round(Float.parseFloat(resetAfter)))
                                    .onFailure(NumberFormatException.class).transform(IllegalStateException::new);
                        }
                        return Uni.createFrom().failure(IllegalStateException::new);
                    }
                    return Uni.createFrom().voidItem();
                })
                .onItem().invoke(res -> promise.complete(res))
                .onItemOrFailure().call(() -> {
                    Duration resetAfter = getResetAfterDuration();
                    if (!resetAfter.isZero() && !resetAfter.isNegative()) {
                        LOG.debug("Delaying demand signal for next request for buckets matching key {} by {}s",
                                bucketKey, resetAfter.getSeconds());

                        return Uni.createFrom().voidItem().onItem().delayIt().by(resetAfter).invoke(this::request);
                    }
                    return Uni.createFrom().voidItem().invoke(this::request);
                })
                .subscribe()
                .with(x -> {
                }, promise::fail);
    }

    @Override
    public void onFailure(Throwable failure) {
        // do nothing
    }

    @Override
    public void onCompletion() {
        // do nothing
    }

    @Override
    public void onSubscribe(Flow.Subscription s) {
        this.subscription = s;
        s.request(1);
    }
}
