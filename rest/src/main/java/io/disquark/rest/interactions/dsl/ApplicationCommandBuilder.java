package io.disquark.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import io.disquark.rest.interactions.ApplicationCommandInteraction;
import io.disquark.rest.resources.application.command.ApplicationCommand;
import io.disquark.rest.resources.interactions.Interaction;

public class ApplicationCommandBuilder
        extends AbstractApplicationCommandBuilder<ApplicationCommandInteraction, ApplicationCommandOptionBuilder> {

    protected ApplicationCommandBuilder() {
        super(Interaction.Type.APPLICATION_COMMAND, ApplicationCommandInteraction::new);
    }

    @Override
    public ApplicationCommandBuilder type(ApplicationCommand.Type type) {
        this.type = requireNonNull(type, "type");
        return this;
    }
}
