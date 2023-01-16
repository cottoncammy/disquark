package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Request;
import org.example.rest.response.HttpResponse;

import java.util.function.Consumer;
import java.util.function.Function;

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
