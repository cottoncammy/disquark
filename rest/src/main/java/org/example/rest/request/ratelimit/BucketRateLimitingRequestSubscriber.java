package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.MultiSubscriber;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;
import org.slf4j.Logger;
import org.reactivestreams.Subscription;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

class BucketRateLimitingRequestSubscriber implements MultiSubscriber<CompletableRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(BucketRateLimitingRequestSubscriber.class);
    private final BucketCacheKey bucketKey;
    private final Requester<HttpResponse> requester;
    private final Promise<String> bucketPromise;

    private volatile int resetAfter;

    private Subscription subscription;

    public BucketRateLimitingRequestSubscriber(
            BucketCacheKey bucketKey,
            Requester<HttpResponse> requester,
            Promise<String> bucketPromise) {
        this.bucketKey = bucketKey;
        this.requester = requester;
        this.bucketPromise = bucketPromise;
    }

    private Duration getResetAfterDuration() {
        return Duration.between(Instant.now(), Instant.ofEpochSecond(resetAfter));
    }

    @Override
    public void onItem(CompletableRequest item) {
        Promise<HttpResponse> promise = item.getPromise();

        requester.request(item.getRequest())
            .call(response -> {
                String bucket = response.getHeader("X-RateLimit-Bucket");
                if (bucket != null) {
                    if (bucketKey.getTopLevelResourceValue().isPresent()) {
                        bucket += '-' + bucketKey.getTopLevelResourceValue().get();
                    }

                    bucketPromise.complete(bucket);
                }

                String remaining = response.getHeader("X-RateLimit-Remaining");
                if (remaining != null && Integer.parseInt(remaining) == 0) {
                    String resetAfter = response.getHeader("X-RateLimit-Reset-After");
                    if (resetAfter != null) {
                        LOG.debug("No requests remaining for buckets matching key {} for the next {}", bucketKey, resetAfter);
                        return Uni.createFrom().voidItem()
                                .invoke(() -> this.resetAfter = Math.round(Float.parseFloat(resetAfter)));
                    }
                    return Uni.createFrom().failure(IllegalStateException::new);
                }
                return Uni.createFrom().voidItem();
            })
            .onItemOrFailure().call(() -> {
                Duration resetAfter = getResetAfterDuration();
                if (!resetAfter.isZero() && !resetAfter.isNegative()) {
                    LOG.debug("Delaying next request for buckets matching key {} by {}", bucketKey, resetAfter);
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
