package io.disquark.rest;

import java.time.Instant;
import java.util.Optional;

import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.interaction.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;

abstract class InteractionsTestBase {

    private <T> Interaction<T> buildInteraction(Interaction.Type type, T data) {
        return new Interaction<>(new Snowflake(Instant.now()), new Snowflake(Instant.now()), type, "foo", 1);
    }

    protected String buildPing() {
        return Json.encode(buildInteraction(Interaction.Type.PING, Optional.empty()));
    }

    protected Uni<HttpClientResponse> sendInteraction(DiscordBotClient<?> botClient, String signature, String timestamp,
            String body) {
        RequestOptions options = new RequestOptions().setMethod(HttpMethod.POST).setURI("/");
        if (System.getenv().containsKey("CI")) {
            options.setPort(8080);
        }

        return botClient.getVertx().createHttpClient()
                .request(options)
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
