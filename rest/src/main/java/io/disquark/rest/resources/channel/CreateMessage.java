package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
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
        protected Builder() {
        }
    }
}
