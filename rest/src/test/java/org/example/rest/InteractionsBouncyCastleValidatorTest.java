package org.example.rest;

import io.vertx.mutiny.core.Vertx;
import org.example.rest.request.AccessTokenSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.example.rest.interactions.dsl.InteractionSchema.ping;

class InteractionsBouncyCastleValidatorTest extends InteractionsTestBase {
    private static DiscordBotClient<?> botClient;

    @BeforeAll
    static void init() {
        botClient = DiscordBotClient.create(Vertx.vertx(), AccessTokenSource.DUMMY);
    }

    @Test
    // TODO
    void testPing() {
        botClient.on(ping());
        assertPongReceived(botClient, null, null);
    }
}
