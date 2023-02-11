package io.disquark.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import io.disquark.rest.interactions.CompletableInteraction;
import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.application.command.ApplicationCommand;
import io.disquark.rest.resources.interactions.ApplicationCommandInteractionDataOption;
import io.disquark.rest.resources.interactions.Interaction;
import io.smallrye.mutiny.tuples.Functions;
import io.vertx.mutiny.core.http.HttpServerResponse;

public abstract class AbstractApplicationCommandBuilder<C extends CompletableInteraction<Interaction.ApplicationCommandData>, O extends AbstractApplicationCommandOptionBuilder<O>>
        implements InteractionSchema<Interaction.ApplicationCommandData, C> {
    private final Interaction.Type interactionType;
    private final Functions.Function3<Interaction<Interaction.ApplicationCommandData>, HttpServerResponse, DiscordInteractionsClient<?>, C> completableInteractionFunction;
    private final List<O> options = new ArrayList<>();

    @Nullable
    private Snowflake id;
    @Nullable
    private String name;
    @Nullable
    protected ApplicationCommand.Type type;
    protected Predicate<Optional<Snowflake>> guildIdPredicate = guildId -> true;

    protected AbstractApplicationCommandBuilder(
            Interaction.Type interactionType,
            Functions.Function3<Interaction<Interaction.ApplicationCommandData>, HttpServerResponse, DiscordInteractionsClient<?>, C> completableInteractionFunction) {
        this.interactionType = interactionType;
        this.completableInteractionFunction = completableInteractionFunction;
    }

    public AbstractApplicationCommandBuilder<C, O> id(Snowflake id) {
        this.id = requireNonNull(id, "id");
        return this;
    }

    public AbstractApplicationCommandBuilder<C, O> name(String name) {
        this.name = requireNonNull(name, "name");
        return this;
    }

    protected AbstractApplicationCommandBuilder<C, O> type(ApplicationCommand.Type type) {
        this.type = requireNonNull(type, "type");
        return this;
    }

    public AbstractApplicationCommandBuilder<C, O> with(O option) {
        options.add(requireNonNull(option, "option"));
        return this;
    }

    public GuildIdStage<C, O, AbstractApplicationCommandBuilder<C, O>> guildId() {
        return new GuildIdStage<>(this);
    }

    @Override
    public boolean validate(Interaction<Interaction.ApplicationCommandData> interaction) {
        List<ApplicationCommandInteractionDataOption> interactionOptions = interaction.data()
                .flatMap(Interaction.ApplicationCommandData::options)
                .orElse(Collections.emptyList());

        for (O option : options) {
            boolean hasOption = false;
            for (ApplicationCommandInteractionDataOption interactionOption : interactionOptions) {
                if (option.test(interactionOption)) {
                    hasOption = true;
                    break;
                }
            }

            if (!hasOption) {
                return false;
            }
        }

        Optional<Interaction.ApplicationCommandData> data = interaction.data();
        return interaction.type() == interactionType &&
                (id == null || Objects.equals(id, data.map(Interaction.ApplicationCommandData::id).orElse(null))) &&
                (name == null || Objects.equals(name, data.map(Interaction.ApplicationCommandData::name).orElse(null))) &&
                (type == null || Objects.equals(type, data.map(Interaction.ApplicationCommandData::type).orElse(null))) &&
                guildIdPredicate.test(interaction.guildId());
    }

    @Override
    public C getCompletableInteraction(Interaction<Interaction.ApplicationCommandData> interaction, HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        return completableInteractionFunction.apply(interaction, response, interactionsClient);
    }
}
