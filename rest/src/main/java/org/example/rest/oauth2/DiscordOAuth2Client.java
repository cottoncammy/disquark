package org.example.rest.oauth2;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.DiscordClient;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.HttpClientRequester;
import org.example.rest.request.Requester;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;
import org.example.rest.request.user.GetUserConnections;
import org.example.rest.resources.User;

public class DiscordOAuth2Client extends DiscordClient {

    public static Builder builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder(requireNonNull(vertx), requireNonNull(tokenSource));
    }

    public static DiscordOAuth2Client create(Vertx vertx, AccessTokenSource tokenSource) {
        return builder(vertx, tokenSource).build();
    }

    public static DiscordOAuth2Client create(BearerTokenSource tokenSource) {
        return create(requireNonNull(tokenSource).getVertx(), tokenSource);
    }

    private DiscordOAuth2Client(
            Vertx vertx,
            Requester requester,
            AccessTokenSource tokenSource,
            GlobalRateLimiter globalRateLimiter) {
        super(vertx, requester, tokenSource, globalRateLimiter);
    }

    public Multi<User.Connection> getUserConnections() {
        return requester.request(new GetUserConnections().asRequest())
                .flatMap(res -> res.as(User.Connection[].class))
                .onItem().disjoint();
    }

    public static class Builder extends DiscordClient.Builder<DiscordOAuth2Client> {

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        public DiscordOAuth2Client build() {
            rateLimitStrategy = rateLimitStrategy == null ? RateLimitStrategy.ALL : rateLimitStrategy;
            return new DiscordOAuth2Client(
                    vertx,
                    requester == null ? rateLimitStrategy.apply(HttpClientRequester.create(vertx)) : requester,
                    tokenSource,
                    globalRateLimiter == null ? rateLimitStrategy.getGlobalRateLimiter() : globalRateLimiter);
        }
    }
}
