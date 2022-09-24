package org.example.rest.request;

import io.vertx.mutiny.core.http.HttpClientRequest;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static org.example.rest.request.RequestPredicate.always;

public class RequestConsumer {
    private final Consumer<HttpClientRequest> delegate;
    private final RequestPredicate predicate;

    public static RequestConsumer from(Consumer<HttpClientRequest> delegate, RequestPredicate predicate) {
        return new RequestConsumer(requireNonNull(delegate), requireNonNull(predicate));
    }

    public static RequestConsumer from(Consumer<HttpClientRequest> delegate) {
        return from(delegate, always());
    }

    private RequestConsumer(Consumer<HttpClientRequest> delegate, RequestPredicate predicate) {
        this.delegate = delegate;
        this.predicate = predicate;
    }

    public void accept(HttpClientRequest request) {
        if (predicate.test(request)) {
            delegate.accept(request);
        }
    }

    public RequestConsumer andThen(RequestConsumer after) {
        requireNonNull(after);
        return RequestConsumer.from(request -> { accept(request); after.accept(request); }, predicate);
    }
}
