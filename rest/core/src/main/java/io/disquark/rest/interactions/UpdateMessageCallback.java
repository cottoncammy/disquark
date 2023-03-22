package io.disquark.rest.interactions;

import java.util.EnumSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.rest.json.interaction.Interaction;
import io.disquark.rest.json.message.AllowedMentions;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.message.MessageEmbed;
import io.disquark.rest.json.messagecomponent.Component;

@ImmutableUni
abstract class UpdateMessageCallback extends AbstractInteractionCallbackUni<Interaction.MessageComponentData> {

    public abstract NullableOptional<Boolean> tts();

    public abstract NullableOptional<String> content();

    public abstract NullableOptional<List<MessageEmbed>> embeds();

    @JsonProperty("allowed_mentions")
    public abstract NullableOptional<AllowedMentions> allowedMentions();

    public abstract NullableOptional<EnumSet<Message.Flag>> flags();

    public abstract NullableOptional<List<Component>> components();

    @Override
    protected Interaction.Response<?> toResponse() {
        return new Interaction.Response<>(Interaction.CallbackType.UPDATE_MESSAGE).withData(this);
    }
}
