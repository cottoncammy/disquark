package org.example.rest.request;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.MultiSubscriber;
import io.vertx.mutiny.core.Promise;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.example.rest.response.Response;
import org.reactivestreams.Subscription;

import java.time.Duration;
import java.time.Instant;

public class RequestSubscriber implements MultiSubscriber<Request> {
    private final Requester requester;

    private volatile int rateLimitResetAfter;
    private volatile Subscription subscription;

    public RequestSubscriber(Requester requester) {
        this.requester = requester;
    }

    @Override
    public void onItem(Request item) {
        Promise<Response> promise = item.responsePromise();

        requester.request(null)
            .call(response -> {
                HttpClientResponse httpResponse = response.getRaw();
                String bucket = httpResponse.getHeader("X-RateLimit-Bucket");

                String remaining = httpResponse.getHeader("X-RateLimit-Remaining");
                if (remaining != null && Integer.parseInt(remaining) == 0) {
                    rateLimitResetAfter = Math.round(Float.parseFloat(httpResponse.getHeader("X-RateLimit-Reset-After")));
                }

                return Uni.createFrom().voidItem();
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
