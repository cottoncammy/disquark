package io.disquark.rest.interactions.dsl;

import io.disquark.rest.interactions.ApplicationCommandInteraction;
import io.disquark.rest.resources.interactions.Interaction;

public class ApplicationCommandBuilder
        extends AbstractApplicationCommandBuilder<ApplicationCommandInteraction, ApplicationCommandOptionBuilder> {

    protected ApplicationCommandBuilder() {
        super(Interaction.Type.APPLICATION_COMMAND, ApplicationCommandInteraction::new);
    }
}
