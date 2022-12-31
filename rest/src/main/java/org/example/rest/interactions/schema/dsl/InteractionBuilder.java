package org.example.rest.interactions.schema.dsl;

import org.example.rest.resources.Snowflake;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class InteractionBuilder<T> {
    protected Predicate<Optional<Snowflake>> guildIdValidator;

    protected void setGuildIdValidator(Predicate<Optional<Snowflake>> guildIdValidator) {
        this.guildIdValidator = guildIdValidator;
    }

    @SuppressWarnings("unchecked")
    public <S extends InteractionBuilder<T>> GuildIdStage<S> guildId() {
        return (GuildIdStage<S>) new GuildIdStage<>(this);
    }

    public abstract T data();
}
