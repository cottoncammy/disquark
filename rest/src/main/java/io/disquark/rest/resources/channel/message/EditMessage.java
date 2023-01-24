package io.disquark.rest.resources.channel.message;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.interactions.components.Component;
import io.disquark.rest.resources.partial.PartialAttachment;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
public interface EditMessage extends MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonIgnore
    Snowflake messageId();

    NullableOptional<String> content();

    NullableOptional<List<Message.Embed>> embeds();

    NullableOptional<EnumSet<Message.Flag>> flags();

    @JsonProperty("allowed_mentions")
    NullableOptional<AllowedMentions> allowedMentions();

    NullableOptional<List<Component>> components();

    NullableOptional<List<PartialAttachment>> attachments();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/channels/{channel.id}/messages/{message.id}"))
                .variables(variables("channel.id", channelId().getValue(), "message.id", messageId().getValue()))
                .body(this)
                .files(files())
                .build();
    }

    class Builder extends ImmutableEditMessage.Builder {
        protected Builder() {
        }
    }
}
