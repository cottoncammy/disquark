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
import io.disquark.rest.json.messagecomponent.Component;

@ImmutableUni
abstract class ResponseCallback<T> extends AbstractInteractionCallbackUni<T> {

    public abstract Optional<Boolean> tts();

    public abstract Optional<String> content();

    public abstract Optional<List<MessageEmbed>> embeds();

    @JsonProperty("allowed_mentions")
    public abstract Optional<AllowedMentions> allowedMentions();

    public abstract Optional<EnumSet<Message.Flag>> flags();

    public abstract Optional<List<Component>> components();

    @Override
    protected Interaction.Response<?> toResponse() {
        return new Interaction.Response<>(Interaction.CallbackType.CHANNEL_MESSAGE_WITH_SOURCE).withData(this);
    }
}
