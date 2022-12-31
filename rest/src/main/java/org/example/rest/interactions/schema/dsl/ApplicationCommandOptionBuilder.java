package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.application.command.ApplicationCommand;
import org.example.rest.resources.interactions.Interaction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// TODO differentiate between method option and dsl option
public class ApplicationCommandOptionBuilder extends AbstractStage<ApplicationCommandDataBuilder> implements InteractionSchema<Interaction.ApplicationCommandData> {
    private final List<ApplicationCommandOptionBuilder> options = new ArrayList<>();

    @Nullable
    private String name;
    @Nullable
    private ApplicationCommand.Option.Type type;
    private boolean focused;

    protected ApplicationCommandOptionBuilder(ApplicationCommandDataBuilder previousStage) {
        super(previousStage);
    }

    public static ApplicationCommandOptionBuilder option() {
        return new ApplicationCommandOptionBuilder();
    }

    public ApplicationCommandOptionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ApplicationCommandOptionBuilder type(ApplicationCommand.Option.Type type) {
        this.type = type;
        return this;
    }

    public ApplicationCommandOptionBuilder with(ApplicationCommandOptionBuilder option) {
        options.add(option);
        return this;
    }

    public ApplicationCommandOptionBuilder focused(boolean focused) {
        this.focused = focused;
        return this;
    }

    @Override
    public boolean validate(Interaction<Interaction.ApplicationCommandData> interaction) {
        return false;
    }
}
