package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.example.it.extension.DiscordClients;
import org.example.rest.DiscordBotClient;
import org.example.rest.interactions.DiscordInteractionsClient.Options;
import org.example.rest.interactions.InteractionValidatorFactory;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.interactions.Interaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.example.it.ConfigHelper.configValue;
import static org.example.rest.interactions.dsl.InteractionSchema.ping;

class InteractionsIT {
    private DiscordBotClient<?> botClient;
    private Snowflake applicationId;

    @BeforeAll
    void init() {
        botClient = DiscordBotClient.builder(DiscordClients.getBotClient().getVertx(), configValue("DISCORD_TOKEN", String.class))
                .interactionsClientOptions(new Options().setValidatorFactory(InteractionValidatorFactory.NO_OP))
                .build();
        applicationId = configValue("DISCORD_APPLICATION_ID", Snowflake.class);
    }

    private <T> Interaction<T> buildInteraction(Interaction.Type type, T data) {
        return Interaction.<T>builder()
                .id(Snowflake.create(Instant.now()))
                .applicationId(applicationId)
                .type(type)
                .data(data)
                .token("foo")
                .version(1)
                .build();
    }

    @Test
    void testPing() {
        botClient.on(ping());

        botClient.getVertx().createHttpClient()
                .request(HttpMethod.POST, "/")
                .flatMap(req -> {
                    req.putHeader("X-Signature-Ed25519", "foo");
                    req.putHeader("X-Signature-Timestamp", "bar");
                    return req.send(Json.encode(buildInteraction(Interaction.Type.PING, null)));
                })
                .flatMap(HttpClientResponse::body)
                .map(body -> body.toJsonObject().mapTo(Interaction.Response.class))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertItem(Interaction.Response.create(Interaction.CallbackType.PONG));
    }
}
