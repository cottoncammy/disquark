package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.MultiSubscriber;
import io.vertx.mutiny.core.Promise;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;
import org.reactivestreams.Subscription;

import java.time.Duration;
import java.time.Instant;

class BucketRateLimitingRequestSubscriber implements MultiSubscriber<CompletableRequest> {
    private final Requester<HttpResponse> requester;
    private final Promise<String> bucketPromise;

    private volatile int rateLimitResetAfter;
    private Subscription subscription;

    public BucketRateLimitingRequestSubscriber(Requester<HttpResponse> requester, Promise<String> bucketPromise) {
        this.requester = requester;
        this.bucketPromise = bucketPromise;
    }

    @Override
    public void onItem(CompletableRequest item) {
        Promise<HttpResponse> promise = item.getPromise();

        requester.request(item.getRequest())
            .invoke(response -> {
                HttpClientResponse httpResponse = response.getRaw();
                bucketPromise.complete(httpResponse.getHeader("X-RateLimit-Bucket"));

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