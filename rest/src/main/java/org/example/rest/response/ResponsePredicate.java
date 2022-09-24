package org.example.rest.response;

import io.vertx.mutiny.core.http.HttpClientResponse;

import java.util.function.Predicate;

public interface ResponsePredicate extends Predicate<HttpClientResponse> {

    static ResponsePredicate statusCodeIs(int statusCode) {
        return response -> response.statusCode() == statusCode;
    }
}
