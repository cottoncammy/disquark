package org.example.rest.interactions.schema.dsl;

import org.example.rest.resources.application.command.ApplicationCommand;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractApplicationCommandOptionBuilder<O extends AbstractApplicationCommandOptionBuilder<O>> {
    private final List<O> options = new ArrayList<>();

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
}
