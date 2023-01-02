package org.example.rest.oauth2;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import io.vertx.ext.auth.oauth2.Oauth2Credentials;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.auth.User;
import io.vertx.mutiny.ext.auth.oauth2.OAuth2Auth;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.resources.oauth2.AccessToken;

import javax.annotation.Nullable;

public class BearerTokenSource implements AccessTokenSource {
    private final Vertx vertx;
    private final OAuth2Auth oAuth2;
    private final OAuth2FlowType flowType;
    @Nullable
    private final String code;
    @Nullable
    private final String redirectUrl;

    private volatile User user;

    public static Builder create(Vertx vertx, String clientId, String clientSecret) {
        OAuth2Options options = new OAuth2Options()
                .setClientId(requireNonNull(clientId))
                .setClientSecret(requireNonNull(clientSecret))
                .setSite("https://discord.com/api/oauth2")
                .setAuthorizationPath("/authorize")
                .setTokenPath("/token")
                .setRevocationPath("/token/revoke");
        return new Builder(requireNonNull(vertx), OAuth2Auth.create(vertx, options));
    }

    private BearerTokenSource(Vertx vertx, OAuth2Auth oAuth2, OAuth2FlowType flowType, String code, String redirectUrl) {
        this.vertx = vertx;
        this.oAuth2 = oAuth2;
        this.flowType = flowType;
        this.code = code;
        this.redirectUrl = redirectUrl;
    }

    @Override
    public Uni<AccessToken> getToken() {
        Uni<User> uni = Uni.createFrom().item(user);
        if (user != null) {
            if (user.expired()) {
                uni = oAuth2.refresh(user).invoke(user -> this.user = user);
            }
        } else {
            Oauth2Credentials credentials = new Oauth2Credentials().setFlow(flowType);
            if (flowType == OAuth2FlowType.AUTH_CODE) {
                credentials.setCode(code).setRedirectUri(redirectUrl);
            }
            uni = oAuth2.authenticate(credentials).invoke(user -> this.user = user);
        }

        return uni.map(user -> user.principal().mapTo(AccessToken.class));
    }

    public Vertx getVertx() {
        return vertx;
    }

    public static class Builder {
        final Vertx vertx;
        final OAuth2Auth oAuth2;

        protected Builder(Vertx vertx, OAuth2Auth oAuth2) {
            this.vertx = vertx;
            this.oAuth2 = oAuth2;
        }

        public BearerTokenSource from(String code, String redirectUrl) {
            return new BearerTokenSource(
                    vertx,
                    oAuth2,
                    OAuth2FlowType.AUTH_CODE,
                    requireNonNull(code),
                    requireNonNull(redirectUrl));
        }

        public BearerTokenSource fromClientCredentials() {
            return new BearerTokenSource(vertx, oAuth2, OAuth2FlowType.CLIENT, null, null);
        }
    }
}
