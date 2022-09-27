package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;

public class BucketRateLimitingRequestStream {
    private final Requester requester;
    private final Promise<Void> completionPromise;
    private final BroadcastProcessor<Request> processor;
    private final BucketCacheInserter bucketCacheInserter;

    private BucketRateLimitingRequestStream(
            Requester requester,
            Promise<Void> completionPromise,
            BroadcastProcessor<Request> processor,
            BucketCacheInserter bucketCacheInserter) {
        this.requester = requester;
        this.completionPromise = completionPromise;
        this.processor = processor;
        this.bucketCacheInserter = bucketCacheInserter;
    }

    public BucketRateLimitingRequestStream(Requester requester, BucketCacheInserter bucketCacheInserter) {
        this(requester, Promise.promise(), BroadcastProcessor.create(), bucketCacheInserter);
    }

    public void onNext(Request request) {
        processor.onNext(request);
    }

    public void subscribe() {
        BucketRateLimitingRequestSubscriber subscriber = new BucketRateLimitingRequestSubscriber(requester, bucketCacheInserter);
        processor.subscribe().withSubscriber(subscriber);
        completionPromise.future().subscribe().with(x -> subscriber.onCompletion());
    }

    public void complete() {
        completionPromise.complete();
    }
}
