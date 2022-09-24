package org.example.rest;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.request.Requester;
import org.example.rest.request.channel.message.CreateMessage;
import org.example.rest.resources.channel.message.Message;

public class DiscordBotClient extends DiscordClient {

    public static Builder builder() {
        return new Builder();
    }

    private DiscordBotClient(Vertx vertx, Requester requester) {
        super(vertx, requester);
    }

    public Uni<Message> createMessage(CreateMessage createMessage) {
        return requester.request(createMessage).flatMap(res -> res.as(Message.class));
    }

    public static class Builder extends DiscordClient.Builder<DiscordBotClient> {

        @Override
        public DiscordBotClient build() {
            return new DiscordBotClient(requireNonNull(vertx), requireNonNull(requester));
        }
    }
}
