package org.example.it;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.vertx.mutiny.core.Vertx;
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
import org.example.rest.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.example.it.ConfigHelper.configValue;

@Tag("rate-limit")
class RateLimitStrategyIT {
    private static final int MAX_REQUESTS = 50;

    @Test
    void testGlobalRateLimiting() {
        DiscordBotClient.create(Vertx.vertx(), configValue("DISCORD_TOKEN", String.class))
                .listVoiceRegions().toUni()
                .repeat().atMost(MAX_REQUESTS + 1)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    void testBucketRateLimiting() {
        AtomicInteger remaining = new AtomicInteger();

        DiscordBotClient<HttpResponse> botClient = DiscordBotClient.<HttpResponse>builder(Vertx.vertx(), configValue("DISCORD_TOKEN", String.class))
                .requesterFactory(new RequesterFactory<>() {
                    @Override
                    public Requester<HttpResponse> apply(DiscordClient.Builder<HttpResponse, ? extends DiscordClient<HttpResponse>> builder) {
                        return new Requester<>() {
                            @Override
                            public Uni<HttpResponse> request(Request request) {
                                HttpClientRequester httpRequester = HttpClientRequester.create(builder.getVertx(), builder.getTokenSource(), Bucket4jRateLimiter.create());
                                return httpRequester.request(request).invoke(res -> remaining.set(Integer.parseInt(res.getRaw().getHeader("X-RateLimit-Remaining"))));
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
        DiscordBotClient<HttpResponse> botClient = DiscordBotClient.<HttpResponse>builder(Vertx.vertx(), configValue("DISCORD_TOKEN", String.class))
                .rateLimitStrategy(RateLimitStrategy.BUCKET)
                .build();

        botClient.listVoiceRegions().toUni()
                .repeat().atMost(MAX_REQUESTS + 1)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitItems(MAX_REQUESTS)
                .awaitFailure(t -> {
                    assertInstanceOf(RateLimitException.class, t);
                    assertTrue(((RateLimitException)t).getResponse().global());
                });
    }

    @Test
    void testNoBucketRateLimiting() {
        DiscordBotClient<Response> botClient = DiscordBotClient.builder(Vertx.vertx(), configValue("DISCORD_TOKEN", String.class))
                .rateLimitStrategy(RateLimitStrategy.GLOBAL)
                .build();

        botClient.listVoiceRegions().toUni()
                .repeat().indefinitely()
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitFailure(t -> {
                    assertInstanceOf(RateLimitException.class, t);
                    assertEquals("Bucket", ((RateLimitException)t).getScope());
                });
    }
}
