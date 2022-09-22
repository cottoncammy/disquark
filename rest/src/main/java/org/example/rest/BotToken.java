package org.example.rest;

import io.vertx.core.Future;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.TokenType;
import org.example.rest.resources.oauth2.AccessToken;

public class BotToken implements AccessTokenSource {
    private final Future<AccessToken> token;

    public static BotToken of(String token) {
        return new BotToken(token);
    }

    private BotToken(String token) {
        this.token = Future.succeededFuture(AccessToken.builder().accessToken(token).tokenType(TokenType.BOT).build());
    }

    @Override
    public Future<AccessToken> getToken() {
        return token;
    }
}
