package org.example.rest.oauth2;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.example.rest.DiscordClient;
import org.example.rest.request.Requester;
import org.example.rest.request.user.GetUserConnections;
import org.example.rest.resources.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DiscordOAuth2Client extends DiscordClient {

    public static Builder builder() {
        return new Builder();
    }

    private DiscordOAuth2Client(Vertx vertx, Requester requester) {
        super(vertx, requester);
    }

    public Future<List<User.Connection>> getUserConnections() {
        return requester.request(new GetUserConnections(), vertx.getOrCreateContext())
                .compose(res -> res.as(User.Connection[].class))
                .map(arr -> new ArrayList<>(Arrays.asList(arr)));
    }

    public static class Builder extends DiscordClient.Builder<DiscordOAuth2Client> {

        @Override
        public DiscordOAuth2Client build() {
            return new DiscordOAuth2Client(Objects.requireNonNull(vertx), Objects.requireNonNull(requester));
        }
    }
}
