package io.disquark.rest.interactions.dsl;

import java.util.Collections;
import java.util.Objects;

import io.disquark.rest.resources.application.command.ApplicationCommand;
import io.disquark.rest.resources.interactions.ApplicationCommandInteractionDataOption;

public class ApplicationCommandAutocompleteOptionBuilder
        extends AbstractApplicationCommandOptionBuilder<ApplicationCommandAutocompleteOptionBuilder> {
    private boolean focused;

    public static ApplicationCommandAutocompleteOptionBuilder option() {
        return new ApplicationCommandAutocompleteOptionBuilder();
    }

    @Override
    public ApplicationCommandAutocompleteOptionBuilder name(String name) {
        super.name(name);
        return this;
    }

    @Override
    public ApplicationCommandAutocompleteOptionBuilder type(ApplicationCommand.Option.Type type) {
        super.type(type);
        return this;
    }

    @Override
    public ApplicationCommandAutocompleteOptionBuilder with(ApplicationCommandAutocompleteOptionBuilder option) {
        super.with(option);
        return this;
    }

    public ApplicationCommandAutocompleteOptionBuilder focused() {
        focused = true;
        return this;
    }

    @Override
    public boolean test(ApplicationCommandInteractionDataOption interactionOption) {
        for (ApplicationCommandAutocompleteOptionBuilder option : options) {
            if (interactionOption.options().orElse(Collections.emptyList()).stream().noneMatch(option)) {
                return false;
            }
        }

        return (name == null || name.equals(interactionOption.name())) &&
                (type == null || type.equals(interactionOption.type())) &&
                Objects.equals(focused, interactionOption.focused().orElse(false));
    }
}
