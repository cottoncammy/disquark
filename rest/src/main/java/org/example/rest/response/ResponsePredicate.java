package org.example.rest.response;

import io.vertx.core.http.HttpClientResponse;

import java.util.function.Predicate;

public interface ResponsePredicate extends Predicate<HttpClientResponse> {

    static ResponsePredicate isStatusCode() {
        
    }
}
