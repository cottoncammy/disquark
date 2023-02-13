package io.disquark.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import io.disquark.rest.resources.application.command.ApplicationCommand;
import io.disquark.rest.resources.interactions.ApplicationCommandInteractionDataOption;

public abstract class AbstractApplicationCommandOptionBuilder<O extends AbstractApplicationCommandOptionBuilder<O>>
        implements Predicate<ApplicationCommandInteractionDataOption> {
    protected final List<O> options = new ArrayList<>();

    @Nullable
    protected String name;
    @Nullable
    protected ApplicationCommand.Option.Type type;

    protected AbstractApplicationCommandOptionBuilder() {
    }

    protected AbstractApplicationCommandOptionBuilder<O> name(String name) {
        this.name = requireNonNull(name, "name");
        return this;
    }

    protected AbstractApplicationCommandOptionBuilder<O> type(ApplicationCommand.Option.Type type) {
        this.type = requireNonNull(type, "type");
        return this;
    }

    protected AbstractApplicationCommandOptionBuilder<O> with(O option) {
        options.add(requireNonNull(option, "option"));
        return this;
    }

    @Override
    public boolean test(ApplicationCommandInteractionDataOption interactionOption) {
        for (O option : options) {
            if (interactionOption.options().orElse(Collections.emptyList()).stream().noneMatch(option)) {
                return false;
            }
        }

        return (name == null || name.equals(interactionOption.name()))
                && (type == null || type.equals(interactionOption.type()));
    }
}
