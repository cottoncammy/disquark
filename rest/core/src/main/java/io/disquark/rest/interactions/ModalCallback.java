package io.disquark.rest.interactions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.interaction.Interaction;
import io.disquark.rest.json.messagecomponent.Component;

@ImmutableUni
abstract class ModalCallback<T> extends AbstractInteractionCallbackUni<T> {

    @JsonProperty("custom_id")
    public abstract String customId();

    public abstract String title();

    public abstract List<Component> components();

    @Override
    protected Interaction.Response<?> toResponse() {
        return new Interaction.Response<>(Interaction.CallbackType.MODAL).withData(this);
    }
}
