package org.example.rest.oauth2;

import static java.util.Objects.requireNonNull;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import io.vertx.ext.auth.oauth2.Oauth2Credentials;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.resources.oauth2.AccessToken;

import javax.annotation.Nullable;

public class BearerTokenSource implements AccessTokenSource {
    private final OAuth2Auth oAuth2;
    private final OAuth2FlowType flowType;
    @Nullable
    private final String code;
    @Nullable
    private final String redirectUrl;

    private volatile User user;

    public static Builder create(Vertx vertx, String clientId, String clientSecret) {
        OAuth2Options options = new OAuth2Options()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setSite("https://discord.com/api/oauth2")
                .setAuthorizationPath("/authorize")
                .setTokenPath("/token")
                .setRevocationPath("/token/revoke");
        return new Builder(OAuth2Auth.create(vertx, options));
    }

    private BearerTokenSource(OAuth2Auth oAuth2, OAuth2FlowType flowType, String code, String redirectUrl) {
        this.oAuth2 = oAuth2;
        this.flowType = flowType;
        this.code = code;
        this.redirectUrl = redirectUrl;
    }

    @Override
    public Future<AccessToken> getToken() {
        Promise<User> promise = Promise.promise();
        if (user != null) {
            if (user.expired()) {
                oAuth2.refresh(user)
                    .onSuccess(user -> {
                        this.user = user;
                        promise.complete(user);
                    })
                    .onFailure(promise::fail);
            } else {
                promise.complete(user);
            }
        } else {
            Oauth2Credentials credentials = new Oauth2Credentials().setFlow(flowType);
            if (flowType == OAuth2FlowType.AUTH_CODE) {
                credentials.setCode(code).setRedirectUri(redirectUrl);
            }

            oAuth2.authenticate(credentials)
                .onSuccess(user -> {
                    this.user = user;
                    promise.complete(user);
                })
                .onFailure(promise::fail);
        }

        return promise.future().map(user -> user.principal().mapTo(AccessToken.class));
    }

    public static class Builder {
        final OAuth2Auth oAuth2;

        protected Builder(OAuth2Auth oAuth2) {
            this.oAuth2 = oAuth2;
        }

        public BearerTokenSource from(String code, String redirectUrl) {
            return new BearerTokenSource(oAuth2, OAuth2FlowType.AUTH_CODE, requireNonNull(code), requireNonNull(redirectUrl));
        }

        public BearerTokenSource fromClientCredentials() {
            return new BearerTokenSource(oAuth2, OAuth2FlowType.CLIENT, null, null);
        }
    }
}
