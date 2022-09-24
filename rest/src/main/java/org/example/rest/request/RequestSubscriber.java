package org.example.rest.request;

import io.smallrye.mutiny.subscription.MultiSubscriber;
import org.reactivestreams.Subscription;

public class RequestSubscriber implements MultiSubscriber<Request> {

    @Override
    public void onItem(Request item) {

    }

    @Override
    public void onFailure(Throwable failure) {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onSubscribe(Subscription s) {

    }
}
