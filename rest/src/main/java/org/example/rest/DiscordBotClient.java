package org.example.rest;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.Requester;
import org.example.rest.request.RequesterFactory;
import org.example.rest.request.channel.message.CreateMessage;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.response.Response;

import static java.util.Objects.requireNonNull;

public class DiscordBotClient<T extends Response> extends DiscordClient<T> {

    public static <T extends Response> Builder<T> builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder<>(requireNonNull(vertx), requireNonNull(tokenSource));
    }

    public static <T extends Response> Builder<T> builder(Vertx vertx, String token) {
        return builder(vertx, BotToken.create(requireNonNull(token)));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordBotClient<T> create(Vertx vertx, AccessTokenSource tokenSource) {
        return (DiscordBotClient<T>) builder(vertx, tokenSource).build();
    }

    public static <T extends Response> DiscordBotClient<T> create(Vertx vertx, String token) {
        return create(vertx, BotToken.create(token));
    }

    private DiscordBotClient(Vertx vertx, Requester<T> requester) {
        super(vertx, requester);
    }

    public Uni<Message> createMessage(CreateMessage createMessage) {
        return requester.request(createMessage.asRequest()).flatMap(res -> res.as(Message.class));
    }

    public static class Builder<T extends Response> extends DiscordClient.Builder<T, DiscordBotClient<T>> {

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        @SuppressWarnings("unchecked")
        public DiscordBotClient<T> build() {
            if (requesterFactory == null) {
                requesterFactory = (RequesterFactory<T>) RequesterFactory.DEFAULT_HTTP_REQUESTER;
            }
            return new DiscordBotClient<>(vertx, requesterFactory.apply(this));
        }
    }
}
