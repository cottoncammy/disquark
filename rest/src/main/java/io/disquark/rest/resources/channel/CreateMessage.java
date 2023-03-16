package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.AllowedMentions;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.components.Component;
import io.disquark.rest.resources.partial.PartialAttachment;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class CreateMessage extends AbstractRequestUni<Message> implements MultipartRequest {

    @JsonIgnore
    public abstract Snowflake channelId();

    public abstract Optional<String> content();

    public abstract Optional<String> nonce();

    public abstract Optional<Boolean> tts();

    public abstract Optional<List<Message.Embed>> embeds();

    @JsonProperty("allowed_mentions")
    public abstract Optional<AllowedMentions> allowedMentions();

    @JsonProperty("message_reference")
    public abstract Optional<Message.Reference> messageReference();

    public abstract Optional<List<Component>> components();

    @JsonProperty("sticker_ids")
    public abstract Optional<List<Snowflake>> stickerIds();

    public abstract Optional<List<PartialAttachment>> attachments();

    public abstract Optional<EnumSet<Message.Flag>> flags();

    @Override
    public void subscribe(UniSubscriber<? super Message> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Message.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/messages"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .files(files())
                .build();
    }
}
