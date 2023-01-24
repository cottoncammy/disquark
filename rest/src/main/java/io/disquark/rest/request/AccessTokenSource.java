package io.disquark.rest.request;

import io.disquark.rest.resources.oauth2.AccessToken;
import io.disquark.rest.resources.oauth2.TokenType;
import io.smallrye.mutiny.Uni;

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
