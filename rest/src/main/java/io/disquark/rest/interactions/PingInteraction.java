package io.disquark.rest.interactions;

import io.disquark.rest.resources.interactions.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;

public class PingInteraction extends CompletableInteraction<Void> {

    public PingInteraction(
            RoutingContext context,
            Interaction<Void> interaction,
            DiscordInteractionsClient<?> interactionsClient) {
        super(context, interaction, interactionsClient);
    }

    Uni<Void> pong() {
        return serialize(Interaction.Response.create(Interaction.CallbackType.PONG))
                .invoke(() -> LOG.debug("Responding to interaction {} with pong", interaction.id().getValueAsString()));
    }
}
