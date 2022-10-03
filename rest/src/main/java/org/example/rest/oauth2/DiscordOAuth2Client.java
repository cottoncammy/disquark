package org.example.rest.oauth2;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.DiscordClient;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.Requester;
import org.example.rest.request.RequesterFactory;
import org.example.rest.request.user.GetUserConnections;
import org.example.rest.resources.User;
import org.example.rest.response.Response;

public class DiscordOAuth2Client<T extends Response> extends DiscordClient<T> {

    public static <T extends Response> Builder<T> builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder<>(requireNonNull(vertx), requireNonNull(tokenSource));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordOAuth2Client<T> create(Vertx vertx, AccessTokenSource tokenSource) {
        return (DiscordOAuth2Client<T>) builder(vertx, tokenSource).build();
    }

    public static <T extends Response> DiscordOAuth2Client<T> create(BearerTokenSource tokenSource) {
        return create(requireNonNull(tokenSource).getVertx(), tokenSource);
    }

    private DiscordOAuth2Client(Vertx vertx, Requester<T> requester) {
        super(vertx, requester);
    }

    public Multi<User.Connection> getUserConnections() {
        return requester.request(new GetUserConnections().asRequest())
                .flatMap(res -> res.as(User.Connection[].class))
                .onItem().disjoint();
    }

    public static class Builder<T extends Response> extends DiscordClient.Builder<T, DiscordOAuth2Client<T>> {

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        public DiscordOAuth2Client<T> build() {
            if (requesterFactory == null) {
                requesterFactory = RequesterFactory.HTTP_REQUESTER_FACTORY;
            }
            return new DiscordOAuth2Client<>(vertx, requesterFactory.apply(this));
        }
    }
}
