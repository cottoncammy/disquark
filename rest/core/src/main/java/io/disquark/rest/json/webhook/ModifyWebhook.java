package io.disquark.rest.json.webhook;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class ModifyWebhook extends AbstractRequestUni<Webhook> implements Auditable {

    @JsonIgnore
    public abstract Snowflake webhookId();

    public abstract Optional<String> name();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Buffer> avatar();

    @JsonProperty("channel_id")
    public abstract Optional<Snowflake> channelId();

    @Override
    public void subscribe(UniSubscriber<? super Webhook> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Webhook.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/webhooks/{webhook.id}"))
                .variables(variables("webhook.id", webhookId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
