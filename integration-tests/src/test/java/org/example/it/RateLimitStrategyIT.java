package org.example.it;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import org.example.it.config.ConfigValue;
import org.example.rest.DiscordBotClient;
import org.example.rest.DiscordClient;
import org.example.rest.request.HttpClientRequester;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;
import org.example.rest.request.RequesterFactory;
import org.example.rest.request.ratelimit.Bucket4jRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;
import org.example.rest.response.HttpResponse;
import org.example.rest.response.RateLimitException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.it.config.ConfigHelper.configValue;
import static org.junit.jupiter.api.Assertions.*;

@Tag("rate-limit")
@ExtendWith(SomeExtension2.class)
class RateLimitStrategyIT {
    private static final Vertx VERTX = DiscordClients.getVertx();
    private static final int MAX_REQUESTS = 50;

    private String token;

    @BeforeAll
    void init(@ConfigValue("DISCORD_TOKEN") String token) {
        this.token = token;
    }

    @Test
    void testGlobalRateLimiting() {
        DiscordBotClient.create(VERTX, token)
                .listVoiceRegions().toUni()
                .repeat().atMost(MAX_REQUESTS + 1)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    void testBucketRateLimiting() {
        AtomicInteger remaining = new AtomicInteger();

        DiscordBotClient<?> botClient = DiscordBotClient.<HttpResponse>builder(VERTX, token)
                .requesterFactory(new RequesterFactory<>() {
                    @Override
                    public Requester<HttpResponse> apply(DiscordClient.Builder<HttpResponse, ? extends DiscordClient<HttpResponse>> builder) {
                        return new Requester<>() {
                            @Override
                            public Uni<HttpResponse> request(Request request) {
                                HttpClientRequester httpRequester = HttpClientRequester.create(builder.getVertx(), builder.getTokenSource(), Bucket4jRateLimiter.create());
                                return httpRequester.request(request).invoke(res -> remaining.set(Integer.parseInt(res.getHeader("X-RateLimit-Remaining"))));
                            }
                        };
                    }
                })
                .build();

        botClient.listVoiceRegions().toUni()
                .replaceWith(remaining.get())
                .repeat().until(val -> val == 0)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .request(1)
                .assertCompleted();
    }

    @Test
    void testNoGlobalRateLimiting() {
        DiscordBotClient<?> botClient = DiscordBotClient.<HttpResponse>builder(VERTX, token)
                .rateLimitStrategy(RateLimitStrategy.BUCKET)
                .build();

        botClient.listVoiceRegions().toUni()
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

        botClient.listVoiceRegions().toUni()
                .repeat().indefinitely()
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitFailure(t -> {
                    assertInstanceOf(RateLimitException.class, t);
                    assertEquals(Optional.of(RateLimitException.Scope.USER), ((RateLimitException) t).getScope());
                });
    }
}
