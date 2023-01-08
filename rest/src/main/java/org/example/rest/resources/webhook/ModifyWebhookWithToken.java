package org.example.rest.resources.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;
import org.example.immutables.ImmutableJson;
import org.example.rest.jackson.ImageDataSerializer;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface ModifyWebhookWithToken extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake webhookId();

    @JsonIgnore
    String webhookToken();

    Optional<String> name();

    @JsonSerialize(using = ImageDataSerializer.class)
    Optional<Buffer> avatar();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/webhooks/{webhook.id}/{webhook.token}", false))
                .variables(variables("webhook.id", webhookId().getValue(), "webhook.token", webhookToken()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableModifyWebhookWithToken.Builder {
        protected Builder() {}
    }
}
