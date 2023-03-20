package io.disquark.rest.json.interaction;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;

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
import io.disquark.rest.json.messagecomponent.Component;
import io.disquark.rest.json.partial.PartialAttachment;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class EditOriginalInteractionResponse extends AbstractRequestUni<Message> implements MultipartRequest {

    @JsonIgnore
    public abstract Snowflake applicationId();

    @JsonIgnore
    public abstract String interactionToken();

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
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/webhooks/{application.id}/{interaction.token}/messages/@original",
                        false))
                .variables(variables("application.id", applicationId().getValue(), "interaction.token", interactionToken()))
                .body(this)
                .files(files())
                .build();
    }
}
