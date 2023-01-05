package org.example.rest;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.interactions.Interaction;

import java.time.Instant;

abstract class InteractionsTestBase {

    protected <T> Interaction<T> buildInteraction(Interaction.Type type, T data) {
        return Interaction.<T>builder()
                .id(Snowflake.create(Instant.now()))
                .applicationId(Snowflake.create(Instant.now()))
                .type(type)
                .data(data)
                .token("foo")
                .version(1)
                .build();
    }

    protected void assertPongReceived(DiscordBotClient<?> botClient, String signature, String timestamp) {
        botClient.getVertx().createHttpClient()
                .request(HttpMethod.POST, "/")
                .flatMap(req -> {
                    req.putHeader("X-Signature-Ed25519", signature);
                    req.putHeader("X-Signature-Timestamp", timestamp);
                    return req.send(Json.encode(buildInteraction(Interaction.Type.PING, null)));
                })
                .flatMap(HttpClientResponse::body)
                .map(body -> body.toJsonObject().mapTo(Interaction.Response.class))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertItem(Interaction.Response.create(Interaction.CallbackType.PONG));
    }
}
