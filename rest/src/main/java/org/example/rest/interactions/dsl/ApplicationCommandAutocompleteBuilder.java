package org.example.rest.interactions.dsl;

import org.example.rest.interactions.ApplicationCommandAutocompleteInteraction;
import org.example.rest.resources.interactions.Interaction;

public class ApplicationCommandAutocompleteBuilder extends AbstractApplicationCommandBuilder<ApplicationCommandAutocompleteInteraction, ApplicationCommandAutocompleteOptionBuilder> {

    protected ApplicationCommandAutocompleteBuilder() {
        super(Interaction.Type.APPLICATION_COMMAND_AUTOCOMPLETE, ApplicationCommandAutocompleteInteraction::new);
    }
}
