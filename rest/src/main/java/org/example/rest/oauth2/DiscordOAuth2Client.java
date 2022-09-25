package org.example.rest.oauth2;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.DiscordClient;
import org.example.rest.request.Requester;
import org.example.rest.request.user.GetUserConnections;
import org.example.rest.resources.User;

public class DiscordOAuth2Client extends DiscordClient {

    public static Builder builder() {
        return new Builder();
    }

    private DiscordOAuth2Client(Vertx vertx, Requester requester) {
        super(vertx, requester);
    }

    public Multi<User.Connection> getUserConnections() {
        return requester.request(new GetUserConnections())
                .flatMap(res -> res.as(User.Connection[].class))
                .onItem().disjoint();
    }

    public static class Builder extends DiscordClient.Builder<DiscordOAuth2Client> {

        @Override
        public DiscordOAuth2Client build() {
            return new DiscordOAuth2Client(requireNonNull(vertx), requireNonNull(requester));
        }
    }
}
