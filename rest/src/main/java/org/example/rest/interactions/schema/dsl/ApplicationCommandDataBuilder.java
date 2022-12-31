package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.application.command.ApplicationCommand;
import org.example.rest.resources.interactions.Interaction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApplicationCommandDataBuilder extends AbstractStage<ApplicationCommandBuilder> implements InteractionSchema<Interaction.ApplicationCommandData> {
    private final List<ApplicationCommandOptionBuilder> options = new ArrayList<>();

    @Nullable
    private Snowflake id;
    @Nullable
    private String name;
    @Nullable
    private ApplicationCommand.Type type;

    protected ApplicationCommandDataBuilder(ApplicationCommandBuilder previousStage) {
        super(previousStage);
    }

    public ApplicationCommandDataBuilder id(Snowflake id) {
        this.id = id;
        return this;
    }

    public ApplicationCommandDataBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ApplicationCommandDataBuilder type(ApplicationCommand.Type type) {
        this.type = type;
        return this;
    }

    public ApplicationCommandDataBuilder with(ApplicationCommandOptionBuilder option) {
        options.add(option);
        return this;
    }

    @Override
    public boolean validate(Interaction<Interaction.ApplicationCommandData> interaction) {
        return previousStage.validate(interaction) &&
                interaction.data().isPresent() &&
                Objects.equals(id, interaction.data().get().id()) &&
                Objects.equals(name, interaction.data().get().name()) &&
                Objects.equals(type, interaction.data().get().type());
    }
}
