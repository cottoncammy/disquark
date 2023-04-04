package io.disquark.rest.interactions;

import io.disquark.rest.json.interaction.Interaction;
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
        return new PongCallbackUni(context, interaction, interactionsClient).replaceWithVoid();
    }
}
