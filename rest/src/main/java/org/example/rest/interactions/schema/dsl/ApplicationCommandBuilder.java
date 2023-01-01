package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.ApplicationCommandInteraction;
import org.example.rest.resources.interactions.Interaction;

public class ApplicationCommandBuilder extends AbstractApplicationCommandBuilder<ApplicationCommandInteraction, ApplicationCommandOptionBuilder> {

    protected ApplicationCommandBuilder() {
        super(Interaction.Type.APPLICATION_COMMAND, ApplicationCommandInteraction::new);
    }
}
