package io.disquark.rest.json.webhook;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.message.AllowedMentions;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.message.MessageEmbed;
import io.disquark.rest.json.message.PartialAttachment;
import io.disquark.rest.json.messagecomponent.Component;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class EditWebhookMessage extends AbstractRequestUni<Message> implements MultipartRequest {

    @JsonIgnore
    public abstract Snowflake webhookId();

    @JsonIgnore
    public abstract String webhookToken();

    @JsonIgnore
    public abstract Snowflake messageId();

    @JsonIgnore
    public abstract Optional<Snowflake> threadId();

    public abstract NullableOptional<String> content();

    public abstract NullableOptional<List<MessageEmbed>> embeds();

    @JsonProperty("allowed_mentions")
    public abstract NullableOptional<AllowedMentions> allowedMentions();

    public abstract NullableOptional<List<Component>> components();

    public abstract NullableOptional<List<PartialAttachment>> attachments();

    @Override
    public void subscribe(UniSubscriber<? super Message> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Message.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
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
}
