package org.example.rest;

import io.vertx.core.Future;
import org.example.rest.request.TokenProvider;
import org.example.rest.request.TokenType;

public class BotToken implements TokenProvider {
    private final String token;

    public static BotToken of(String token) {
        return new BotToken(token);
    }

    private BotToken(String token) {
        this.token = token;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.BOT;
    }

    @Override
    public Future<String> getToken() {
        return Future.succeededFuture(token);
    }
}
