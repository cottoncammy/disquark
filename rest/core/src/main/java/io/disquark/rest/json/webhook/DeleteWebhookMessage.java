package io.disquark.rest.json.webhook;

import java.util.Optional;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

@ImmutableUni
abstract class DeleteWebhookMessage extends AbstractRequestUni<Void> {

    public abstract Snowflake webhookId();

    public abstract String webhookToken();

    public abstract Snowflake messageId();

    public abstract Optional<Snowflake> threadId();

    @Override
    public void subscribe(UniSubscriber<? super Void> downstream) {
        requester().request(asRequest()).replaceWithVoid().subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("webhook.id", webhookId().getValue(), "webhook.token", webhookToken(), "message.id",
                messageId().getValue());
        if (threadId().isPresent()) {
            json.put("thread_id", threadId().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.DELETE,
                        "/webhooks/{webhook.id}/{webhook.token}/messages/{message.id}{?thread_id}", false))
                .variables(Variables.variables(json))
                .build();
    }
}
