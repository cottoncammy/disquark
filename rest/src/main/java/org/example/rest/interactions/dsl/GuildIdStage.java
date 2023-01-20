package org.example.rest.interactions.dsl;

import java.util.Objects;
import java.util.Optional;

import org.example.rest.interactions.CompletableInteraction;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.interactions.Interaction;

public class GuildIdStage<C extends CompletableInteraction<Interaction.ApplicationCommandData>, O extends AbstractApplicationCommandOptionBuilder<O>, T extends AbstractApplicationCommandBuilder<C, O>>
        extends OptionalValueStage<T, Snowflake> {

    protected GuildIdStage(T previousStage) {
        super(previousStage,
                (t, expected) -> t.guildIdPredicate = actual -> Objects.equals(expected, actual.orElse(null)),
                t -> t.guildIdPredicate = Optional::isPresent,
                t -> t.guildIdPredicate = Optional::isEmpty);
    }
}
