package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Context;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.MultiSubscriber;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.HttpClientRequester;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;
import org.example.rest.response.RateLimitException;
import org.slf4j.Logger;
import org.reactivestreams.Subscription;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.example.rest.request.HttpClientRequester.FALLBACK_REQUEST_ID;
import static org.example.rest.request.HttpClientRequester.REQUEST_ID;
import static org.example.rest.util.ExceptionPredicate.is;

class BucketRateLimitingRequestSubscriber implements MultiSubscriber<CompletableRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(BucketRateLimitingRequestSubscriber.class);
    private static final String ITERATION = "iteration";

    private final BucketCacheKey bucketKey;
    private final Requester<HttpResponse> requester;
    private final Promise<String> bucketPromise;

    private volatile int resetAfter;
    private volatile boolean bucketPromiseCompleted;

    private Subscription subscription;

    public BucketRateLimitingRequestSubscriber(
            BucketCacheKey bucketKey,
            Requester<HttpResponse> requester,
            Promise<String> bucketPromise) {
        this.bucketKey = bucketKey;
        this.requester = requester;
        this.bucketPromise = bucketPromise;
    }

    private Uni<HttpResponse> requestWithContext(Request request, Context ctx) {
        return requester.request(request)
            .onFailure(is(RateLimitException.class).and(x -> ctx.getOrElse(ITERATION, () -> 0) < 5)).retry().when(multi -> {
                return multi.onItem().castTo(RateLimitException.class)
                        .call(rateLimit -> {
                            Duration retryAfter = Duration.between(Instant.now(),
                                    Instant.ofEpochSecond(Math.round(rateLimit.getResponse().retryAfter())));

                            if (!retryAfter.isZero() && !retryAfter.isNegative()) {
                                return Uni.createFrom().voidItem().onItem().delayIt().by(retryAfter);
                            }
                            return Uni.createFrom().voidItem();
                        })
                        .invoke(() -> {
                            ctx.put(ITERATION, ctx.getOrElse(ITERATION, () -> 0) + 1);
                            LOG.debug("Retrying outgoing request {} due to rate limit, attempts: {}",
                                    ctx.getOrElse(REQUEST_ID, FALLBACK_REQUEST_ID), ctx.get(ITERATION));
                        });
            });
    }

    private Duration getResetAfterDuration() {
        return Duration.between(Instant.now(), Instant.ofEpochSecond(resetAfter));
    }

    @Override
    public void onItem(CompletableRequest item) {
        Promise<HttpResponse> promise = item.getPromise();

        // TODO can't fail with bucketPromise, need to wait for first item between requestPromise and bucketPromise
        // then fail if no termination after timeout
        Uni.createFrom().context(ctx -> requestWithContext(item.getRequest(), ctx))
            .onFailure().invoke(bucketPromise::tryFail)
            .invoke(response -> {
                String bucket = response.getHeader("X-RateLimit-Bucket");
                if (bucket != null && !bucketPromiseCompleted) {
                    if (bucketKey.getTopLevelResourceValue().isPresent()) {
                        bucket += '-' + bucketKey.getTopLevelResourceValue().get();
                    }

                    bucketPromiseCompleted = bucketPromise.tryComplete(bucket);
                } else if (!bucketPromiseCompleted) {
                    LOG.debug("Request matching bucket key {} didn't return a bucket value", bucketKey);
                    bucketPromiseCompleted = bucketPromise.tryFail(new NoSuchElementException());
                }
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
            .onItemOrFailure().call(() -> {
                Duration resetAfter = getResetAfterDuration();
                if (!resetAfter.isZero() && !resetAfter.isNegative()) {
                    LOG.debug("Delaying demand signal for next request for buckets matching key {} by {}s",
                            bucketKey, resetAfter.getSeconds());

                    return Uni.createFrom().voidItem().onItem().delayIt().by(resetAfter);
                }
                return Uni.createFrom().voidItem();
            })
            .onItemOrFailure().invoke(() -> {
                LOG.debug("Signalling demand for next request for buckets matching key {}", bucketKey);
                subscription.request(1);
            })
            .subscribe()
            .with(promise::complete, promise::fail);
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
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        s.request(1);
    }
}
