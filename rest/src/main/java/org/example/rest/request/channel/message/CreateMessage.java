package org.example.rest.request.channel.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.uritemplate.Variables;
import org.example.rest.request.*;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.channel.message.AllowedMentions;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.components.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@ImmutableJson
public interface CreateMessage extends MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    Optional<String> content();

    Optional<Boolean> tts();

    Optional<List<Message.Embed>> embeds();

    @JsonProperty("allowed_mentions")
    Optional<AllowedMentions> allowedMentions();

    @JsonProperty("message_reference")
    Optional<Message.Reference> messageReference();

    Optional<List<Component>> components();

    @JsonProperty("sticker_ids")
    Optional<List<Snowflake>> stickerIds();

    Optional<List<Message.Attachment>> attachments();

    Optional<EnumSet<Message.Flag>> flags();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.from(HttpMethod.POST, "/channels/{channel.id}/messages"))
                .variables(Variables.variables().set("channel.id", channelId().asString()))
                .body(this)
                .files(files())
                .build();
    }

    class Builder extends ImmutableCreateMessage.Builder {
        protected Builder() {}
    }
}
