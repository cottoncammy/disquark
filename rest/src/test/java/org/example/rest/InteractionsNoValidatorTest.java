package org.example.rest;

import io.vertx.core.json.Json;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.interactions.InteractionValidatorFactory;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.resources.interactions.Interaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.example.rest.interactions.dsl.InteractionSchema.ping;

@Disabled
class InteractionsNoValidatorTest extends InteractionsTestBase {
    private static DiscordBotClient<?> botClient;

    @BeforeAll
    static void init() {
        botClient = DiscordBotClient.builder(Vertx.vertx(), AccessTokenSource.DUMMY)
                .interactionsClientOptions(new DiscordInteractionsClient.Options()
                        .setVerifyKey("foo")
                        .setValidatorFactory(InteractionValidatorFactory.NO_OP))
                .build();
    }

    @Test
    void testPing() {
        botClient.on(ping());
        assertPongReceived(botClient, "foo", "bar", Json.encode(buildPing()));
    }
}
