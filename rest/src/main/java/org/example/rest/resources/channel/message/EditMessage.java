package org.example.rest.resources.channel.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.request.Endpoint;
import org.example.rest.request.MultipartRequest;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.interactions.components.Component;
import org.example.nullableoptional.NullableOptional;
import org.example.rest.resources.partial.PartialAttachment;

import java.util.EnumSet;
import java.util.List;

import static org.example.rest.util.Variables.variables;

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
        protected Builder() {}
    }
}
