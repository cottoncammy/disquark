package org.example.rest;

import io.vertx.mutiny.core.Vertx;
import org.example.rest.interactions.DiscordInteractionsClient.Options;
import org.example.rest.interactions.InteractionValidatorFactory;
import org.example.rest.request.AccessTokenSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.example.rest.interactions.dsl.InteractionSchema.ping;

class InteractionsNoValidatorTest extends InteractionsTestBase {
    private static DiscordBotClient<?> botClient;

    @BeforeAll
    static void init() {
        botClient = DiscordBotClient.builder(Vertx.vertx(), AccessTokenSource.DUMMY)
                .interactionsClientOptions(new Options().setValidatorFactory(InteractionValidatorFactory.NO_OP))
                .build();
    }

    @Test
    void testPing() {
        botClient.on(ping());
        assertPongReceived(botClient, "foo", "bar");
    }
}
