package org.example.rest.request;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.vertx.mutiny.core.Promise;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.example.rest.request.ratelimit.GlobalRateLimiter;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class RequestProcessor {
    private final BroadcastProcessor<Request> processor;
    // TODO replace with bucket cache consumer that is invoked to update the cache
    private final Map<Endpoint, String> bucketCache;
    private final Promise<Void> completionPromise;
    private final GlobalRateLimiter rateLimiter;
    private final Requester requester;

    private volatile long rateLimitResetAfter;

    private RequestProcessor(BroadcastProcessor<Request> processor, Map<Endpoint, String> bucketCache, GlobalRateLimiter rateLimiter, Requester requester) {
        this.processor = processor;
        this.bucketCache = bucketCache;
        this.rateLimiter = rateLimiter;
        this.requester = requester;
    }

    // TODO associate a Promise<Response> with each Request
    public void process(Request request) {
        processor.onNext(request);
    }

    public void subscribe() {
        RequestSubscriber subscriber = new RequestSubscriber();
        processor.onItem().transformToUniAndConcatenate(request -> requester.request(null))
                .invoke(response -> {
                    HttpClientResponse httpResponse = response.getRaw();
                    String bucket = httpResponse.getHeader("X-RateLimit-Bucket");

                    String remaining = httpResponse.getHeader("X-RateLimit-Remaining");
                    if (remaining != null && Integer.parseInt(remaining) == 0) {
                        httpResponse.getHeader("X-RateLimit-Reset-After");
                    }

                    if (Boolean.parseBoolean(httpResponse.getHeader("X-RateLimit-Global"))) {
                        httpResponse.getHeader("Retry-After");
                    }
                })
                .onTermination().call(() -> {
                    return Uni.createFrom().nullItem()
                            .onItem().delayIt().by(Duration.between(Instant.now(), Instant.ofEpochSecond(rateLimitResetAfter)))
                            .onSubscription().invoke(s -> s.request(1));
                })
                .subscribe()
                .withSubscriber(subscriber);

        completionPromise.future().subscribe().with(x -> subscriber.onCompletion());
    }

    public void complete() {
        completionPromise.complete();
    }
}
