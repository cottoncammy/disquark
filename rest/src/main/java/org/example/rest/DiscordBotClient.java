package org.example.rest;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.HttpClientRequester;
import org.example.rest.request.Requester;
import org.example.rest.request.channel.message.CreateMessage;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;
import org.example.rest.resources.channel.message.Message;

import static java.util.Objects.requireNonNull;

public class DiscordBotClient extends DiscordClient {

    public static Builder builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder(requireNonNull(vertx), requireNonNull(tokenSource));
    }

    public static DiscordBotClient create(Vertx vertx, AccessTokenSource tokenSource) {
        return builder(vertx, tokenSource).build();
    }

    public static DiscordBotClient create(Vertx vertx, String token) {
        return create(vertx, BotToken.create(token));
    }

    private DiscordBotClient(
            Vertx vertx,
            Requester requester,
            AccessTokenSource tokenSource,
            GlobalRateLimiter globalRateLimiter) {
        super(vertx, requester, tokenSource, globalRateLimiter);
    }

    public Uni<Message> createMessage(CreateMessage createMessage) {
        return requester.request(createMessage.asRequest()).flatMap(res -> res.as(Message.class));
    }

    public static class Builder extends DiscordClient.Builder<DiscordBotClient> {

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        public DiscordBotClient build() {
            rateLimitStrategy = rateLimitStrategy == null ? RateLimitStrategy.ALL : rateLimitStrategy;
            return new DiscordBotClient(
                    vertx,
                    requester == null ? rateLimitStrategy.apply(HttpClientRequester.create(vertx)) : requester,
                    tokenSource,
                    globalRateLimiter == null ? rateLimitStrategy.getGlobalRateLimiter() : globalRateLimiter);
        }
    }
}
