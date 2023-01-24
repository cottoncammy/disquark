package io.disquark.rest.resources.webhook;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.AllowedMentions;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.components.Component;
import io.disquark.rest.resources.partial.PartialAttachment;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

@ImmutableJson
public interface EditWebhookMessage extends MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake webhookId();

    @JsonIgnore
    String webhookToken();

    @JsonIgnore
    Snowflake messageId();

    @JsonIgnore
    Optional<Snowflake> threadId();

    Optional<String> content();

    Optional<List<Message.Embed>> embeds();

    @JsonProperty("allowed_mentions")
    Optional<AllowedMentions> allowedMentions();

    Optional<List<Component>> components();

    Optional<List<PartialAttachment>> attachments();

    @Override
    default Request asRequest() {
        JsonObject json = JsonObject.of("webhook.id", webhookId().getValue(), "webhook.token", webhookToken(), "message.id",
                messageId().getValue());
        if (threadId().isPresent()) {
            json.put("thread_id", threadId().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH,
                        "/webhooks/{webhook.id}/{webhook.token}/messages/{message.id}{?thread_id}", false))
                .variables(Variables.variables(json))
                .body(this)
                .files(files())
                .build();
    }

    class Builder extends ImmutableEditWebhookMessage.Builder {
        protected Builder() {
        }
    }
}
