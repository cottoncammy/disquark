package org.example.rest.request;

import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.vertx.mutiny.core.Promise;

public class RequestProcessor {
    private final Requester requester;
    private final Promise<Void> completionPromise;
    private final BroadcastProcessor<Request> processor;

    private RequestProcessor(Requester requester, BroadcastProcessor<Request> processor) {
        this.requester = requester;
        this.completionPromise = Promise.promise();
        this.processor = processor;
    }

    public void process(Request request) {
        processor.onNext(request);
    }

    public void subscribe() {
        RequestSubscriber subscriber = new RequestSubscriber(requester);
        processor.subscribe().withSubscriber(subscriber);
        completionPromise.future().subscribe().with(x -> subscriber.onCompletion());
    }

    public void complete() {
        completionPromise.complete();
    }
}
