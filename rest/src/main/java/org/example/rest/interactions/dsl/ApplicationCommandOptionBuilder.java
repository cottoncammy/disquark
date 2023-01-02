package org.example.rest.interactions.dsl;

public class ApplicationCommandOptionBuilder extends AbstractApplicationCommandOptionBuilder<ApplicationCommandOptionBuilder> {

    public static ApplicationCommandOptionBuilder option() {
        return new ApplicationCommandOptionBuilder();
    }
}
