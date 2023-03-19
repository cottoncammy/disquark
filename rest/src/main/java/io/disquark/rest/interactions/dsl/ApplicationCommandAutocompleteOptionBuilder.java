package io.disquark.rest.interactions.dsl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.disquark.rest.json.command.ApplicationCommand;
import io.disquark.rest.json.interaction.ApplicationCommandInteractionDataOption;

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
        List<ApplicationCommandInteractionDataOption> interactionOptions = interactionOption.options()
                .orElse(Collections.emptyList());

        for (ApplicationCommandAutocompleteOptionBuilder option : options) {
            boolean hasOption = false;
            for (ApplicationCommandInteractionDataOption optionOption : interactionOptions) {
                if (option.test(optionOption)) {
                    hasOption = true;
                    break;
                }
            }

            if (!hasOption) {
                return false;
            }
        }

        return (name == null || name.equals(interactionOption.name())) &&
                (type == null || type.equals(interactionOption.type())) &&
                Objects.equals(focused, interactionOption.focused().orElse(false));
    }
}
