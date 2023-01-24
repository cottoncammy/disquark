package io.disquark.rest.interactions.dsl;

import io.disquark.rest.interactions.ApplicationCommandAutocompleteInteraction;
import io.disquark.rest.resources.interactions.Interaction;

public class ApplicationCommandAutocompleteBuilder extends
        AbstractApplicationCommandBuilder<ApplicationCommandAutocompleteInteraction, ApplicationCommandAutocompleteOptionBuilder> {

    protected ApplicationCommandAutocompleteBuilder() {
        super(Interaction.Type.APPLICATION_COMMAND_AUTOCOMPLETE, ApplicationCommandAutocompleteInteraction::new);
    }
}
