package org.example.rest.request;

import io.smallrye.mutiny.Uni;
import org.example.rest.resources.oauth2.AccessToken;
import org.example.rest.resources.oauth2.TokenType;

@FunctionalInterface
public interface AccessTokenSource {

    AccessTokenSource DUMMY = new AccessTokenSource() {
        @Override
        public Uni<AccessToken> getToken() {
            return Uni.createFrom().item(AccessToken.builder().tokenType(TokenType.BOT).accessToken("").build());
        }
    };

    Uni<AccessToken> getToken();
}
