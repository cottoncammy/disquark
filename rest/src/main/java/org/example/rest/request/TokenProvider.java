package org.example.rest.request;

import io.vertx.core.Future;

public interface TokenProvider {

    TokenType getTokenType();

    Future<String> getToken();
}
