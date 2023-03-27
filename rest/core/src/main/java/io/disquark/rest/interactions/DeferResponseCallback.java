package io.disquark.rest.interactions;

import java.util.EnumSet;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.interaction.Interaction;
import io.disquark.rest.json.message.Message;

@ImmutableUni
abstract class DeferResponseCallback<T> extends AbstractInteractionCallbackUni<T> {

    public abstract boolean ephemeral();

    @Override
    protected Interaction.Response<?> toResponse() {
        Interaction.Response<?> response = new Interaction.Response<>(
                Interaction.CallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE);
        return ephemeral() ? response.withData(Interaction.CallbackData.of().withFlags(EnumSet.of(Message.Flag.EPHEMERAL)))
                : response;
    }
}
