package org.example.rest.interactions.dsl;

import io.smallrye.mutiny.tuples.Functions;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.interactions.CompletableInteraction;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.application.command.ApplicationCommand;
import org.example.rest.resources.interactions.ApplicationCommandInteractionDataOption;
import org.example.rest.resources.interactions.Interaction;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractApplicationCommandBuilder<C extends CompletableInteraction<Interaction.ApplicationCommandData>, O extends AbstractApplicationCommandOptionBuilder<O>> implements InteractionSchema<Interaction.ApplicationCommandData, C> {
    private final Interaction.Type interactionType;
    private final Functions.Function3<Interaction<Interaction.ApplicationCommandData>, HttpServerResponse, DiscordInteractionsClient<?>, C> completableInteractionFunction;

    protected final List<O> options = new ArrayList<>();

    @Nullable
    protected Snowflake id;
    @Nullable
    protected String name;
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
        this.id = id;
        return this;
    }

    public AbstractApplicationCommandBuilder<C, O> name(String name) {
        this.name = name;
        return this;
    }

    public AbstractApplicationCommandBuilder<C, O> type(ApplicationCommand.Type type) {
        this.type = type;
        return this;
    }

    public AbstractApplicationCommandBuilder<C, O> with(O option) {
        options.add(option);
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
    public C getCompletableInteraction(Interaction<Interaction.ApplicationCommandData> interaction, HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient) {
        return completableInteractionFunction.apply(interaction, response, interactionsClient);
    }
}
