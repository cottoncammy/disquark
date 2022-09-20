package org.example.rest;

import static java.util.Objects.requireNonNull;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
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

    public Future<Message> createMessage(CreateMessage createMessage) {
        return requester.request(createMessage, vertx.getOrCreateContext()).compose(res -> res.as(Message.class));
    }

    public static class Builder extends DiscordClient.Builder<DiscordBotClient> {

        @Override
        public DiscordBotClient build() {
            return new DiscordBotClient(requireNonNull(vertx), requireNonNull(requester));
        }
    }
}
