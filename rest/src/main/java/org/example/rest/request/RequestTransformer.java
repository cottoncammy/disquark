package org.example.rest.request;

import io.vertx.core.http.HttpClientRequest;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class RequestTransformer {
    private final Consumer<HttpClientRequest> consumer;
    private final RequestPredicate predicate;

    public static RequestTransformer of(Consumer<HttpClientRequest> consumer, RequestPredicate predicate) {
        return new RequestTransformer(requireNonNull(consumer), requireNonNull(predicate));
    }

    public static RequestTransformer of(Consumer<HttpClientRequest> consumer) {
        return of(consumer, RequestPredicate.always());
    }

    private RequestTransformer(Consumer<HttpClientRequest> consumer, RequestPredicate predicate) {
        this.consumer = consumer;
        this.predicate = predicate;
    }

    public void accept(HttpClientRequest request) {
        if (predicate.test(request)) {
            consumer.accept(request);
        }
    }

    public RequestTransformer andThen(RequestTransformer after) {
        requireNonNull(after);
        return RequestTransformer.of(request -> { accept(request); after.accept(request); }, predicate);
    }
}
