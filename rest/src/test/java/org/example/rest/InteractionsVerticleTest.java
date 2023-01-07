package org.example.rest;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.interactions.InteractionValidatorFactory;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.resources.interactions.Interaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.example.rest.interactions.dsl.InteractionSchema.ping;

class InteractionsVerticleTest extends InteractionsTestBase {
    private static DiscordBotClient<?> botClient;

    @BeforeAll
    static void init() {
        botClient = DiscordBotClient.builder(Vertx.vertx(), AccessTokenSource.DUMMY)
                .interactionsClientOptions(new DiscordInteractionsClient.Options()
                        .setVerifyKey("foo")
                        .setValidatorFactory(InteractionValidatorFactory.NO_OP))
                .build();

        botClient.on(ping());
    }

    @AfterAll
    static void cleanup() {
        botClient.getVertx().closeAndAwait();
    }

    @Test
    void testPongRespond() {
        sendInteraction(botClient, "foo", "bar", buildPing())
                .flatMap(HttpClientResponse::body)
                .map(buf -> buf.toJsonObject().mapTo(Interaction.Response.class))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertItem(Interaction.Response.create(Interaction.CallbackType.PONG));
    }

    @Test
    void testPingReceive() {
        botClient.on(ping())
                .onRequest().call(() -> sendInteraction(botClient, "foo", "bar", buildPing()))
                .onItem().transform(x -> 1)
                .subscribe().withSubscriber(AssertSubscriber.create())
                .awaitNextItem()
                .assertItems(1);
    }

    @Test
    void testNoSignature() {
        sendInteraction(botClient, null, "foo", "{}")
                .map(HttpClientResponse::statusCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertItem(401);
    }

    @Test
    void testNoTimestamp() {
        sendInteraction(botClient, "foo", null, "{}")
                .map(HttpClientResponse::statusCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertItem(401);
    }

    @Test
    void testNoBody() {
        sendInteraction(botClient, "foo", "bar", null)
                .map(HttpClientResponse::statusCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertItem(400);
    }

    @Test
    void testInvalidBody() {
        sendInteraction(botClient, "foo", "bar", "{}")
                .map(HttpClientResponse::statusCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertItem(400);
    }
}
