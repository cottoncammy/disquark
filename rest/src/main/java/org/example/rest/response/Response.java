package org.example.rest.response;

import io.vertx.core.Future;

@FunctionalInterface
public interface Response {

    <T> Future<T> as(Class<T> type);
}
