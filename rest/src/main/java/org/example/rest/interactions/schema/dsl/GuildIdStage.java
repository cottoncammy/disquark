package org.example.rest.interactions.schema.dsl;

import org.example.rest.resources.Snowflake;

import java.util.Objects;
import java.util.Optional;

public class GuildIdStage<T extends InteractionBuilder<?>> extends OptionalValueStage<T, Snowflake> {

    protected GuildIdStage(T previousStage) {
        super(previousStage,
                (s, expected) -> s.setGuildIdValidator(actual -> Objects.equals(expected, actual.orElse(null))),
                s -> s.setGuildIdValidator(Optional::isPresent),
                s -> s.setGuildIdValidator(Optional::isEmpty));
    }
}
