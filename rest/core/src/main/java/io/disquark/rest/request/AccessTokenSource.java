package io.disquark.rest.request;

import io.disquark.rest.json.oauth2.AccessToken;
import io.disquark.rest.json.oauth2.TokenType;
import io.smallrye.mutiny.Uni;

@FunctionalInterface
public interface AccessTokenSource {

    AccessTokenSource DUMMY = new AccessTokenSource() {
        @Override
        public Uni<AccessToken> getToken() {
            return Uni.createFrom().item(new AccessToken(TokenType.BOT, ""));
        }
    };

    Uni<AccessToken> getToken();
}
