package io.disquark.rest;

import static io.disquark.rest.interactions.dsl.InteractionSchema.ping;

import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.interactions.InteractionsValidatorFactory;
import io.disquark.rest.json.interaction.Interaction;
import io.disquark.rest.request.AccessTokenSource;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClientResponse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InteractionsVerticleTest extends InteractionsTestBase {
    private static DiscordBotClient<?> botClient;

    @BeforeAll
    static void init() {
        botClient = DiscordBotClient.builder(Vertx.vertx(), AccessTokenSource.DUMMY)
                .interactionsClientOptions(new DiscordInteractionsClient.Options()
                        .setVerifyKey("foo")
                        .setValidatorFactory(InteractionsValidatorFactory.NO_OP))
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
                .assertItem(new Interaction.Response<>(Interaction.CallbackType.PONG));
    }

    @Test
    void testPingReceive() {
        botClient.on(ping())
                .onSubscription().call(() -> sendInteraction(botClient, "foo", "bar", buildPing()))
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
