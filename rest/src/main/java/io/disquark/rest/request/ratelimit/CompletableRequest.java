package io.disquark.rest.request.ratelimit;

import java.util.function.Consumer;

import io.disquark.rest.request.Request;
import io.disquark.rest.response.HttpResponse;
import io.vertx.mutiny.core.Promise;

class CompletableRequest {
    private final Request request;
    private final Consumer<String> bucketConsumer;
    private final Promise<HttpResponse> responsePromise = Promise.promise();

    public CompletableRequest(Request request, Consumer<String> bucketConsumer) {
        this.request = request;
        this.bucketConsumer = bucketConsumer;
    }

    public Request getRequest() {
        return request;
    }

    public Consumer<String> getBucketConsumer() {
        return bucketConsumer;
    }

    public Promise<HttpResponse> getResponsePromise() {
        return responsePromise;
    }
}
