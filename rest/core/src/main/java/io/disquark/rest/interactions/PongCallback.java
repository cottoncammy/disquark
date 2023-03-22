package io.disquark.rest.interactions;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.interaction.Interaction;

@ImmutableUni
abstract class PongCallback extends AbstractInteractionCallbackUni<Void> {

    @Override
    protected Interaction.Response<?> toResponse() {
        return new Interaction.Response<>(Interaction.CallbackType.PONG);
    }
}
