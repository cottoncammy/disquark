package org.example.rest.request;

import io.vertx.mutiny.core.http.HttpClientRequest;

import java.util.function.Predicate;

@FunctionalInterface
public interface RequestPredicate extends Predicate<HttpClientRequest> {

    static RequestPredicate always() {
        return new RequestPredicate() {
            @Override
            public boolean test(HttpClientRequest request) {
                return true;
            }
        };
    }
}
