package org.example.rest.request.ratelimit;

import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Request;
import org.example.rest.response.HttpResponse;

class CompletableRequest {
    private final Request request;
    private final Promise<HttpResponse> promise = Promise.promise();

    public CompletableRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public Promise<HttpResponse> getPromise() {
        return promise;
    }
}
