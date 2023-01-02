package org.example.rest;

import io.smallrye.mutiny.Uni;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.resources.oauth2.TokenType;
import org.example.rest.resources.oauth2.AccessToken;

class BotToken implements AccessTokenSource {
    private final Uni<AccessToken> token;

    public static BotToken create(String token) {
        return new BotToken(token);
    }

    private BotToken(String token) {
        this.token = Uni.createFrom().item(AccessToken.builder().tokenType(TokenType.BOT).accessToken(token).build());
    }

    @Override
    public Uni<AccessToken> getToken() {
        return token;
    }
}
