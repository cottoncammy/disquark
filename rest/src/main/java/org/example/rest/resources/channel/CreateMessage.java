package org.example.rest.resources.channel;

import static org.example.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.MultipartRequest;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.AllowedMentions;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.components.Component;
import org.example.rest.resources.partial.PartialAttachment;

@ImmutableJson
public interface CreateMessage extends MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    Optional<String> content();

    Optional<String> nonce();

    Optional<Boolean> tts();

    Optional<List<Message.Embed>> embeds();

    @JsonProperty("allowed_mentions")
    Optional<AllowedMentions> allowedMentions();

    @JsonProperty("message_reference")
    Optional<Message.Reference> messageReference();

    Optional<List<Component>> components();

    @JsonProperty("sticker_ids")
    Optional<List<Snowflake>> stickerIds();

    Optional<List<PartialAttachment>> attachments();

    Optional<EnumSet<Message.Flag>> flags();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/messages"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .files(files())
                .build();
    }

    class Builder extends ImmutableCreateMessage.Builder {
        protected Builder() {}
    }
}
