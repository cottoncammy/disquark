package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;

class BucketRateLimitingRequestStream {
    private final Requester requester;
    private final Promise<Void> completionPromise;
    private final BroadcastProcessor<Request> processor;

    private BucketRateLimitingRequestStream(
            Requester requester,
            Promise<Void> completionPromise,
            BroadcastProcessor<Request> processor) {
        this.requester = requester;
        this.completionPromise = completionPromise;
        this.processor = processor;
    }

    private BucketRateLimitingRequestStream(Requester requester) {
        this(requester, Promise.promise(), BroadcastProcessor.create());
    }

    public void onNext(Request request) {
        processor.onNext(request);
    }

    public void subscribe() {
        BucketRateLimitingRequestSubscriber subscriber = new BucketRateLimitingRequestSubscriber(requester);
        processor.subscribe().withSubscriber(subscriber);
        completionPromise.future().subscribe().with(x -> subscriber.onCompletion());
    }

    public void complete() {
        completionPromise.complete();
    }
}
