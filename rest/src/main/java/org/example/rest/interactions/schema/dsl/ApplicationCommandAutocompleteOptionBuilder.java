package org.example.rest.interactions.schema.dsl;

public class ApplicationCommandAutocompleteOptionBuilder extends AbstractApplicationCommandOptionBuilder<ApplicationCommandAutocompleteOptionBuilder> {
    private boolean focused;

    public static ApplicationCommandAutocompleteOptionBuilder option() {
        return new ApplicationCommandAutocompleteOptionBuilder();
    }

    public ApplicationCommandAutocompleteOptionBuilder focused() {
        focused = true;
        return this;
    }
}
