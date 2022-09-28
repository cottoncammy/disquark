package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;

import java.time.Duration;

class BucketRateLimitingRequestStream {
    private final Requester requester;
    private final Promise<String> bucketPromise = Promise.promise();
    private final BroadcastProcessor<Request> processor = BroadcastProcessor.create();

    private volatile boolean subscribed;

    public BucketRateLimitingRequestStream(Requester requester) {
        this.requester = requester;
    }

    public void onNext(Request request) {
        processor.onNext(request);
    }

    public void subscribe() {
        BucketRateLimitingRequestSubscriber s = new BucketRateLimitingRequestSubscriber(requester, bucketPromise);
        Uni.createFrom().voidItem().invoke(() -> subscribed = true)
                .onItem().transformToMulti(x -> processor)
                .ifNoItem().after(Duration.ofSeconds(30)).recoverWithCompletion()
                .onCompletion().invoke(() -> subscribed = false)
                .subscribe().withSubscriber(s);
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public Uni<String> getBucket() {
        return bucketPromise.future();
    }
}
