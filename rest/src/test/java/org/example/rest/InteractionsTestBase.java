package org.example.rest;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.interactions.Interaction;

import java.time.Instant;

abstract class InteractionsTestBase {

    private <T> Interaction<T> buildInteraction(Interaction.Type type, T data) {
        return Interaction.<T>builder()
                .id(Snowflake.create(Instant.now()))
                .applicationId(Snowflake.create(Instant.now()))
                .type(type)
                .data(data)
                .token("foo")
                .version(1)
                .build();
    }

    protected Interaction<Void> buildPing() {
        return buildInteraction(Interaction.Type.PING, null);
    }

    protected void assertPongReceived(DiscordBotClient<?> botClient, String signature, String timestamp, String body) {
        botClient.getVertx().createHttpClient()
                .request(HttpMethod.POST, "/")
                .flatMap(req -> {
                    req.putHeader("X-Signature-Ed25519", signature);
                    req.putHeader("X-Signature-Timestamp", timestamp);
                    return req.send(body);
                })
                .flatMap(HttpClientResponse::body)
                .map(buf -> buf.toJsonObject().mapTo(Interaction.Response.class))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertItem(Interaction.Response.create(Interaction.CallbackType.PONG));
    }
}
