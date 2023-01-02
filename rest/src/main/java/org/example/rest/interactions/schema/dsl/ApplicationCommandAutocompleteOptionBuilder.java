package org.example.rest.interactions.schema.dsl;

import org.example.rest.resources.interactions.ApplicationCommandInteractionDataOption;

import java.util.Collections;
import java.util.Objects;

public class ApplicationCommandAutocompleteOptionBuilder extends AbstractApplicationCommandOptionBuilder<ApplicationCommandAutocompleteOptionBuilder> {
    private boolean focused;

    public static ApplicationCommandAutocompleteOptionBuilder option() {
        return new ApplicationCommandAutocompleteOptionBuilder();
    }

    public ApplicationCommandAutocompleteOptionBuilder focused() {
        focused = true;
        return this;
    }

    @Override
    public boolean test(ApplicationCommandInteractionDataOption interactionOption) {
        for (ApplicationCommandAutocompleteOptionBuilder option : options) {
            if (!interactionOption.options().orElse(Collections.emptyList()).stream().anyMatch(option)) {
                return false;
            }
        }

        return Objects.equals(interactionOption.name(), name) &&
                Objects.equals(interactionOption.type(), type) &&
                Objects.equals(interactionOption.focused().orElse(null), focused);
    }
}
