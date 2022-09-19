package org.example.rest.oauth2;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import org.example.rest.request.TokenProvider;
import org.example.rest.request.TokenType;

public class BearerTokenProvider implements TokenProvider {
    private final OAuth2Auth oAuth2;
    private volatile User user;

    public static BearerTokenProvider of(Vertx vertx, String clientId, String clientSecret, String code) {
        OAuth2Options options = new OAuth2Options()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setSite("https://discord.com/api/oauth2")
                .setAuthorizationPath("/authorize")
                .setTokenPath("/token")
                .setRevocationPath("/token/revoke");
        return new BearerTokenProvider(OAuth2Auth.create(vertx, options));
    }

    private BearerTokenProvider(OAuth2Auth oAuth2) {
        this.oAuth2 = oAuth2;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.BEARER;
    }

    @Override
    public Future<String> getToken() {
        if (user != null) {
            if (user.expired()) {
                return oAuth2.refresh(user).map(user -> {
                    this.user = user;
                    return user.get("access_token");
                });
            }
            return user.get("access_token");
        }

        return oAuth2.authenticate(new JsonObject().put("code", "").put("redirect_uri", ""))
                .map(user -> {
                    this.user = user;
                    return user.get("access_token");
                });
    }
}
