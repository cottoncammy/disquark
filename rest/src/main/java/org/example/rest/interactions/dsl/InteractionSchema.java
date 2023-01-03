package org.example.rest.interactions.dsl;

import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.interactions.CompletableInteraction;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.interactions.PingInteraction;
import org.example.rest.resources.interactions.Interaction;

import java.util.function.Predicate;

public interface InteractionSchema<D, C extends CompletableInteraction<D>> {

    static InteractionSchema<Void, PingInteraction> ping() {
        return new InteractionSchema<>() {
            @Override
            public boolean validate(Interaction<Void> interaction) {
                return interaction.type() == Interaction.Type.PING;
            }

            @Override
            public PingInteraction getCompletableInteraction(Interaction<Void> interaction, HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient) {
                return new PingInteraction(interaction, response, interactionsClient);
            }
        };
    }

    static ApplicationCommandBuilder applicationCommand() {
        return new ApplicationCommandBuilder();
    }

    static MessageComponentBuilder messageComponent() {
        return new MessageComponentBuilder();
    }

    static ApplicationCommandAutocompleteBuilder applicationCommandAutocomplete() {
        return new ApplicationCommandAutocompleteBuilder();
    }

    static ModalSubmitBuilder modalSubmit() {
        return new ModalSubmitBuilder();
    }

    boolean validate(Interaction<D> interaction);

    C getCompletableInteraction(Interaction<D> interaction, HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient);
}
