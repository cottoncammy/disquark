package io.disquark.rest.interactions.dsl;

import io.disquark.rest.interactions.ApplicationCommandAutocompleteInteraction;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.interactions.Interaction;

public class ApplicationCommandAutocompleteBuilder extends
        AbstractApplicationCommandBuilder<ApplicationCommandAutocompleteInteraction, ApplicationCommandAutocompleteOptionBuilder> {

    protected ApplicationCommandAutocompleteBuilder() {
        super(Interaction.Type.APPLICATION_COMMAND_AUTOCOMPLETE, ApplicationCommandAutocompleteInteraction::new);
    }

    @Override
    public ApplicationCommandAutocompleteBuilder id(Snowflake id) {
        super.id(id);
        return this;
    }

    @Override
    public ApplicationCommandAutocompleteBuilder name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public ApplicationCommandAutocompleteBuilder with(ApplicationCommandAutocompleteOptionBuilder option) {
        super.with(option);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GuildIdStage<ApplicationCommandAutocompleteInteraction, ApplicationCommandAutocompleteOptionBuilder, ApplicationCommandAutocompleteBuilder> guildId() {
        return new GuildIdStage<>(this);
    }
}
