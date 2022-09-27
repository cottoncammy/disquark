package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.MultiSubscriber;
import io.vertx.mutiny.core.Promise;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;
import org.example.rest.response.Response;
import org.reactivestreams.Subscription;

import java.time.Duration;
import java.time.Instant;

public class BucketRateLimitingRequestSubscriber implements MultiSubscriber<Request> {
    private final Requester requester;
    private final BucketCacheInserter bucketCacheInserter;

    private volatile int rateLimitResetAfter;
    private volatile Subscription subscription;

    public BucketRateLimitingRequestSubscriber(Requester requester, BucketCacheInserter bucketCacheInserter) {
        this.requester = requester;
        this.bucketCacheInserter = bucketCacheInserter;
    }

    @Override
    public void onItem(Request item) {
        Promise<Response> promise = item.responsePromise();

        requester.request(item)
            .invoke(response -> {
                HttpClientResponse httpResponse = response.getRaw();
                String bucket = httpResponse.getHeader("X-RateLimit-Bucket");
                if (bucket != null) {
                    bucketCacheInserter.accept(bucket);
                }

                String remaining = httpResponse.getHeader("X-RateLimit-Remaining");
                if (remaining != null && Integer.parseInt(remaining) == 0) {
                    rateLimitResetAfter = Math.round(Float.parseFloat(httpResponse.getHeader("X-RateLimit-Reset-After")));
                }
            })
            .onTermination().call(() -> {
                return Uni.createFrom().nullItem()
                        .onItem().delayIt().by(Duration.between(Instant.now(), Instant.ofEpochSecond(rateLimitResetAfter)))
                        .onItem().invoke(() -> subscription.request(1));
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
