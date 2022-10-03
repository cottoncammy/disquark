package org.example.rest.request.ratelimit;

import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Request;
import org.example.rest.response.HttpResponse;

class CompletableRequest {
    private final Request request;
    private final Promise<HttpResponse> promise;

    private CompletableRequest(Request request, Promise<HttpResponse> promise) {
        this.request = request;
        this.promise = promise;
    }

    public CompletableRequest(Request request) {
        this(request, Promise.promise());
    }

    public Request getRequest() {
        return request;
    }

    public Promise<HttpResponse> getPromise() {
        return promise;
    }
}
