package io.disquark.rest.interactions.callbacks;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.interaction.Interaction;

@ImmutableUni
abstract class DeferEditCallback extends AbstractInteractionCallbackUni<Interaction.MessageComponentData> {

    @Override
    protected Interaction.Response<?> toResponse() {
        return new Interaction.Response<>(Interaction.CallbackType.DEFERRED_UPDATE_MESSAGE);
    }
}
