package io.disquark.rest.interactions.callbacks;

import java.util.List;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.command.ApplicationCommand;
import io.disquark.rest.json.interaction.Interaction;

@ImmutableUni
abstract class AutocompleteCallback extends AbstractInteractionCallbackUni<Interaction.ApplicationCommandData> {

    public abstract List<ApplicationCommand.OptionChoice> choices();

    @Override
    protected Interaction.Response<?> toResponse() {
        return new Interaction.Response<>(Interaction.CallbackType.APPLICATION_COMMAND_AUTOCOMPLETE_RESULT)
                .withData(this);
    }
}
