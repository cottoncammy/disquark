package io.disquark.rest.interactions.dsl;

import java.util.Objects;
import java.util.Optional;

import io.disquark.rest.interactions.CompletableInteraction;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.interaction.Interaction;

public class GuildIdStage<C extends CompletableInteraction<Interaction.ApplicationCommandData>, O extends AbstractApplicationCommandOptionBuilder<O>, T extends AbstractApplicationCommandBuilder<C, O>>
        extends OptionalValueStage<T, Snowflake> {

    protected GuildIdStage(T previousStage) {
        super(previousStage,
                (t, expected) -> t.guildIdPredicate = actual -> Objects.equals(expected, actual.orElse(null)),
                t -> t.guildIdPredicate = Optional::isPresent,
                t -> t.guildIdPredicate = Optional::isEmpty);
    }
}
