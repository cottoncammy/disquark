package io.disquark.rest.interactions.dsl;

import io.disquark.rest.interactions.CompletableInteraction;
import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.interactions.PingInteraction;
import io.disquark.rest.json.interaction.Interaction;
import io.vertx.mutiny.ext.web.RoutingContext;

public interface InteractionSchema<D, C extends CompletableInteraction<D>> {

    static InteractionSchema<Void, PingInteraction> ping() {
        return new InteractionSchema<>() {
            @Override
            public boolean validate(Interaction<Void> interaction) {
                return interaction.type() == Interaction.Type.PING;
            }

            @Override
            public PingInteraction getCompletableInteraction(RoutingContext context, Interaction<Void> interaction,
                    DiscordInteractionsClient<?> interactionsClient) {
                return new PingInteraction(context, interaction, interactionsClient);
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

    C getCompletableInteraction(RoutingContext context, Interaction<D> interaction,
            DiscordInteractionsClient<?> interactionsClient);
}
