package io.disquark.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.DiscordClient;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.HttpClientRequester;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requester;
import io.disquark.rest.request.RequesterFactory;
import io.disquark.rest.request.ratelimit.Bucket4jRateLimiter;
import io.disquark.rest.request.ratelimit.RateLimitStrategy;
import io.disquark.rest.response.HttpResponse;
import io.disquark.rest.response.RateLimitException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.vertx.mutiny.core.Vertx;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Disabled
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class RateLimitStrategyIT {
    private static final Vertx VERTX = DiscordClients.getVertx();
    private static final int MAX_REQUESTS = 50;

    private String token;
    private Snowflake channelId;

    @BeforeAll
    void init(@ConfigValue("DISCORD_TOKEN") String token, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        this.token = token;
        this.channelId = channelId;
    }

    @Test
    void testGlobalRateLimiting() {
        DiscordBotClient.create(VERTX, token)
                .createMessage(channelId)
                .withContent("foo")
                .repeat().atMost(MAX_REQUESTS + 1)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitItems(MAX_REQUESTS + 1)
                .assertNotTerminated();
    }

    @Test
    void testBucketRateLimiting() {
        AtomicInteger remaining = new AtomicInteger();

        DiscordBotClient<?> botClient = DiscordBotClient.<HttpResponse> builder(VERTX, token)
                .requesterFactory(new RequesterFactory<>() {
                    @Override
                    public Requester<HttpResponse> apply(
                            DiscordClient.Builder<HttpResponse, ? extends DiscordClient<HttpResponse>> builder) {
                        return new Requester<>() {
                            @Override
                            public Uni<HttpResponse> request(Request request) {
                                HttpClientRequester httpRequester = HttpClientRequester.create(builder.getVertx(),
                                        builder.getTokenSource(), Bucket4jRateLimiter.create());
                                return httpRequester.request(request)
                                        .invoke(res -> remaining.set(Integer.parseInt(res.getHeader("X-RateLimit-Remaining"))));
                            }
                        };
                    }
                })
                .build();

        botClient.createMessage(channelId)
                .withContent("foo")
                .replaceWith(remaining.get())
                .repeat().until(val -> val == 0)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .request(1)
                .assertNotTerminated();
    }

    @Test
    void testNoGlobalRateLimiting() {
        DiscordBotClient<?> botClient = DiscordBotClient.<HttpResponse> builder(VERTX, token)
                .rateLimitStrategy(RateLimitStrategy.BUCKET)
                .build();

        botClient.createMessage(channelId)
                .withContent("foo")
                .repeat().atMost(MAX_REQUESTS + 1)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitItems(MAX_REQUESTS)
                .awaitFailure(t -> {
                    assertInstanceOf(RateLimitException.class, t);
                    assertTrue(((RateLimitException) t).getResponse().global());
                });
    }

    @Test
    void testNoBucketRateLimiting() {
        DiscordBotClient<?> botClient = DiscordBotClient.builder(VERTX, token)
                .rateLimitStrategy(RateLimitStrategy.GLOBAL)
                .build();

        botClient.createMessage(channelId)
                .withContent("foo")
                .repeat().indefinitely()
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitFailure(t -> {
                    assertInstanceOf(RateLimitException.class, t);
                    assertEquals(Optional.of(RateLimitException.Scope.USER), ((RateLimitException) t).getScope());
                });
    }
}
