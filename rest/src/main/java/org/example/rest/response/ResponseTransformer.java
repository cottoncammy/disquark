package org.example.rest.response;

import io.vertx.core.Future;
import io.vertx.core.http.HttpClientResponse;

public interface ResponseTransformer {

    static void retry() {
    }

    Future<HttpClientResponse> transform(HttpClientResponse response);
}
