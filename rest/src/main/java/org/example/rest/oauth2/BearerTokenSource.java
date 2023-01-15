package org.example.rest.oauth2;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import io.vertx.ext.auth.oauth2.Oauth2Credentials;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.auth.User;
import io.vertx.mutiny.ext.auth.oauth2.OAuth2Auth;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.resources.oauth2.AccessToken;
import org.example.rest.resources.oauth2.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class BearerTokenSource implements AccessTokenSource {
    private static final Logger LOG = LoggerFactory.getLogger(BearerTokenSource.class);
    private final Vertx vertx;
    private final OAuth2Auth oAuth2;
    private final OAuth2FlowType flowType;

    @Nullable
    private final String code;
    @Nullable
    private final String redirectUrl;
    @Nullable
    private final EnumSet<Scope> scopes;

    private volatile User user;

    public static Builder create(Vertx vertx, String clientId, String clientSecret) {
        OAuth2Options options = new OAuth2Options()
                .setClientId(requireNonNull(clientId, "clientId"))
                .setClientSecret(requireNonNull(clientSecret, "clientSecret"))
                .setSite("https://discord.com/api/oauth2")
                .setAuthorizationPath("/authorize")
                .setTokenPath("/token")
                .setRevocationPath("/token/revoke")
                .setUserAgent(String.format("DiscordBot (%s, %s)", "https://github.com/cameronprater/discord-TODO", "0.1.0"));
        return new Builder(requireNonNull(vertx, "vertx"), OAuth2Auth.create(vertx, options));
    }

    private BearerTokenSource(
            Vertx vertx,
            OAuth2Auth oAuth2,
            OAuth2FlowType flowType,
            String code,
            String redirectUrl,
            EnumSet<Scope> scopes) {
        this.vertx = vertx;
        this.oAuth2 = oAuth2;
        this.flowType = flowType;
        this.code = code;
        this.redirectUrl = redirectUrl;
        this.scopes = scopes;
    }

    @Override
    public Uni<AccessToken> getToken() {
        Uni<User> uni = Uni.createFrom().item(user);
        if (user != null) {
            if (user.expired()) {
                LOG.debug("Refreshing expired access token");
                uni = oAuth2.refresh(user).invoke(user -> this.user = user);
            }
        } else {
            Oauth2Credentials credentials = new Oauth2Credentials().setFlow(flowType);
            if (flowType == OAuth2FlowType.AUTH_CODE) {
                credentials.setCode(code).setRedirectUri(redirectUrl);
            } else {
                credentials.setScopes(scopes.stream().map(Scope::getValue).collect(Collectors.toList()));
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
                    requireNonNull(code, "code"),
                    requireNonNull(redirectUrl, "redirectUrl"),
                    null);
        }

        public BearerTokenSource fromClientCredentials(EnumSet<Scope> scopes) {
            return new BearerTokenSource(vertx, oAuth2, OAuth2FlowType.CLIENT, null, null, requireNonNull(scopes, "scopes"));
        }
    }
}
