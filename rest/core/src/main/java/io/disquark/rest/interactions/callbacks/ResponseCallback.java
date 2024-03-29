package io.disquark.rest.interactions.callbacks;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.interaction.Interaction;
import io.disquark.rest.json.message.AllowedMentions;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.message.MessageEmbed;
import io.disquark.rest.json.message.PartialAttachment;
import io.disquark.rest.json.messagecomponent.Component;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ResponseCallback<T> extends MultipartCallback<T> {

    public abstract Optional<Boolean> tts();

    public abstract Optional<String> content();

    public abstract Optional<List<MessageEmbed>> embeds();

    @JsonProperty("allowed_mentions")
    public abstract Optional<AllowedMentions> allowedMentions();

    public abstract Optional<EnumSet<Message.Flag>> flags();

    public abstract Optional<List<Component>> components();

    public abstract Optional<List<PartialAttachment>> attachments();

    // use a fake request object so we can reuse codec
    @Override
    protected Request toRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, ""))
                .body(this)
                .files(files())
                .build();
    }

    @Override
    protected Interaction.Response<?> toResponse() {
        return new Interaction.Response<>(Interaction.CallbackType.CHANNEL_MESSAGE_WITH_SOURCE)
                .withData(this);
    }
}
