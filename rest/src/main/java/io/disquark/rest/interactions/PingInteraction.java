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
        return serialize(new Interaction.Response<>(Interaction.CallbackType.PONG))
                .invoke(() -> LOG.debug("Responding to interaction {} with pong", interaction.id().getValueAsString()));
    }
}
