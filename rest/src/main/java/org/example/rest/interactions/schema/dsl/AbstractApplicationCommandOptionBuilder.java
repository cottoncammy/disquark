package org.example.rest.interactions.schema.dsl;

import org.example.rest.resources.application.command.ApplicationCommand;
import org.example.rest.resources.interactions.ApplicationCommandInteractionDataOption;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class AbstractApplicationCommandOptionBuilder<O extends AbstractApplicationCommandOptionBuilder<O>> implements Predicate<ApplicationCommandInteractionDataOption> {
    protected final List<O> options = new ArrayList<>();

    @Nullable
    protected String name;
    @Nullable
    protected ApplicationCommand.Option.Type type;

    protected AbstractApplicationCommandOptionBuilder() {}

    public AbstractApplicationCommandOptionBuilder<O> name(String name) {
        this.name = name;
        return this;
    }

    public AbstractApplicationCommandOptionBuilder<O> type(ApplicationCommand.Option.Type type) {
        this.type = type;
        return this;
    }

    public AbstractApplicationCommandOptionBuilder<O> with(O option) {
        options.add(option);
        return this;
    }

    @Override
    public boolean test(ApplicationCommandInteractionDataOption interactionOption) {
        for (O option : options) {
            if (!interactionOption.options().orElse(Collections.emptyList()).stream().anyMatch(option)) {
                return false;
            }
        }

        return Objects.equals(interactionOption.name(), name) && Objects.equals(interactionOption.type(), type);
    }
}
