package io.disquark.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import io.disquark.rest.json.command.ApplicationCommand;
import io.disquark.rest.json.interaction.ApplicationCommandInteractionDataOption;

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
        List<ApplicationCommandInteractionDataOption> interactionOptions = interactionOption.options()
                .orElse(Collections.emptyList());

        for (O option : options) {
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

        return (name == null || name.equals(interactionOption.name()))
                && (type == null || type.equals(interactionOption.type()));
    }
}
