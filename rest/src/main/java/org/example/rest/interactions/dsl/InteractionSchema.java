package org.example.rest.interactions.dsl;

import io.smallrye.mutiny.tuples.Functions;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.interactions.CompletableInteraction;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.interactions.PingInteraction;
import org.example.rest.resources.interactions.Interaction;

import java.util.function.Predicate;

public class InteractionSchema<D, C extends CompletableInteraction<D>> {
    private final Predicate<Interaction<D>> interactionPredicate;
    private final Functions.Function3<Interaction<D>, HttpServerResponse, DiscordInteractionsClient<?>, C> completableInteractionFunction;

    public static InteractionSchema<Void, PingInteraction> ping() {
        return new InteractionSchema<>(interaction -> interaction.type() == Interaction.Type.PING, PingInteraction::new);
    }

    public static ApplicationCommandBuilder applicationCommand() {
        return new ApplicationCommandBuilder();
    }

    public static MessageComponentBuilder messageComponent() {
        return new MessageComponentBuilder();
    }

    public static ApplicationCommandAutocompleteBuilder applicationCommandAutocomplete() {
        return new ApplicationCommandAutocompleteBuilder();
    }

    public static ModalSubmitBuilder modalSubmit() {
        return new ModalSubmitBuilder();
    }

    protected InteractionSchema(
            Predicate<Interaction<D>> interactionPredicate,
            Functions.Function3<Interaction<D>, HttpServerResponse, DiscordInteractionsClient<?>, C> completableInteractionFunction) {
        this.interactionPredicate = interactionPredicate;
        this.completableInteractionFunction = completableInteractionFunction;
    }

    public boolean validate(Interaction<D> interaction) {
        return interactionPredicate.test(interaction);
    }

    public C getCompletableInteraction(Interaction<D> interaction, HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient) {
        return completableInteractionFunction.apply(interaction, response, interactionsClient);
    }
}
