package org.example.rest;

import java.time.Instant;
import java.util.Optional;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;

import org.example.rest.resources.Snowflake;
import org.example.rest.resources.interactions.Interaction;

abstract class InteractionsTestBase {

    private <T> Interaction<T> buildInteraction(Interaction.Type type, T data) {
        return Interaction.<T> builder()
                .id(Snowflake.create(Instant.now()))
                .applicationId(Snowflake.create(Instant.now()))
                .type(type)
                .data(data)
                .token("foo")
                .version(1)
                .build();
    }

    protected String buildPing() {
        return Json.encode(buildInteraction(Interaction.Type.PING, Optional.empty()));
    }

    protected Uni<HttpClientResponse> sendInteraction(DiscordBotClient<?> botClient, String signature, String timestamp,
            String body) {
        return botClient.getVertx().createHttpClient()
                .request(HttpMethod.POST, "/")
                .flatMap(req -> {
                    req.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    req.putHeader("X-Signature-Ed25519", signature);
                    req.putHeader("X-Signature-Timestamp", timestamp);

                    if (body != null) {
                        return req.send(body);
                    }

                    return req.send();
                });
    }
}
