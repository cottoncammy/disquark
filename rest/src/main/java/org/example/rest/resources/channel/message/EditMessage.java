package org.example.rest.resources.channel.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.MultipartRequest;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.interactions.components.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface EditMessage extends MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonIgnore
    Snowflake messageId();

    Optional<String> content();

    Optional<List<Message.Embed>> embeds();

    Optional<EnumSet<Message.Flag>> flags();

    @JsonProperty("allowed_mentions")
    Optional<AllowedMentions> allowedMentions();

    Optional<List<Component>> components();

    Optional<List<Message.Attachment>> attachments();

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
