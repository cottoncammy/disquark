package io.disquark.rest.interactions.dsl;

import io.disquark.rest.resources.application.command.ApplicationCommand;

public class ApplicationCommandOptionBuilder extends AbstractApplicationCommandOptionBuilder<ApplicationCommandOptionBuilder> {

    public static ApplicationCommandOptionBuilder option() {
        return new ApplicationCommandOptionBuilder();
    }

    @Override
    public ApplicationCommandOptionBuilder name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public ApplicationCommandOptionBuilder type(ApplicationCommand.Option.Type type) {
        super.type(type);
        return this;
    }

    @Override
    public ApplicationCommandOptionBuilder with(ApplicationCommandOptionBuilder option) {
        super.with(option);
        return this;
    }
}
