package org.example.rest.resources.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;
import org.example.immutables.ImmutableJson;
import org.example.rest.jackson.ImageDataSerializer;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface ModifyWebhook extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake webhookId();

    Optional<String> name();

    @JsonSerialize(using = ImageDataSerializer.class)
    Optional<Buffer> avatar();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/webhooks/{webhook.id}"))
                .variables(variables("webhook.id", webhookId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyWebhook.Builder {
        protected Builder() {}
    }
}
