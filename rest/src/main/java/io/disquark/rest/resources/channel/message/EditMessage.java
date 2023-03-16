package io.disquark.rest.resources.channel.message;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.interactions.components.Component;
import io.disquark.rest.resources.partial.PartialAttachment;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class EditMessage extends AbstractRequestUni<Message> implements MultipartRequest {

    @JsonIgnore
    public abstract Snowflake channelId();

    @JsonIgnore
    public abstract Snowflake messageId();

    public abstract NullableOptional<String> content();

    public abstract NullableOptional<List<Message.Embed>> embeds();

    public abstract NullableOptional<EnumSet<Message.Flag>> flags();

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
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/channels/{channel.id}/messages/{message.id}"))
                .variables(variables("channel.id", channelId().getValue(), "message.id", messageId().getValue()))
                .body(this)
                .files(files())
                .build();
    }
}
