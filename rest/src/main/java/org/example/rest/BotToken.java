package org.example.rest;

import io.smallrye.mutiny.Uni;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.resources.oauth2.TokenType;
import org.example.rest.resources.oauth2.AccessToken;

public class BotToken implements AccessTokenSource {
    private final Uni<AccessToken> token;

    public static BotToken from(String token) {
        return new BotToken(token);
    }

    private BotToken(String token) {
        this.token = Uni.createFrom().item(AccessToken.builder().accessToken(token).tokenType(TokenType.BOT).build());
    }

    @Override
    public Uni<AccessToken> getToken() {
        return token;
    }
}
