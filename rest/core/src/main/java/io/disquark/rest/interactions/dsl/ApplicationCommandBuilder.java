package io.disquark.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import io.disquark.rest.interactions.ApplicationCommandInteraction;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.command.ApplicationCommand;
import io.disquark.rest.json.interaction.Interaction;

public class ApplicationCommandBuilder
        extends AbstractApplicationCommandBuilder<ApplicationCommandInteraction, ApplicationCommandOptionBuilder> {

    protected ApplicationCommandBuilder() {
        super(Interaction.Type.APPLICATION_COMMAND, ApplicationCommandInteraction::new);
    }

    @Override
    public ApplicationCommandBuilder id(Snowflake id) {
        super.id(id);
        return this;
    }

    @Override
    public ApplicationCommandBuilder name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public ApplicationCommandBuilder type(ApplicationCommand.Type type) {
        this.type = requireNonNull(type, "type");
        return this;
    }

    @Override
    public ApplicationCommandBuilder with(ApplicationCommandOptionBuilder option) {
        super.with(option);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GuildIdStage<ApplicationCommandInteraction, ApplicationCommandOptionBuilder, ApplicationCommandBuilder> guildId() {
        return new GuildIdStage<>(this);
    }
}
