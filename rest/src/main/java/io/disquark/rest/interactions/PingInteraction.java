package io.disquark.rest.interactions;

import io.disquark.rest.resources.interactions.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;

public class PingInteraction extends CompletableInteraction<Void> {

    public PingInteraction(
            Interaction<Void> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    Uni<Void> pong() {
        return serialize(Interaction.Response.create(Interaction.CallbackType.PONG))
                .invoke(json -> LOG.debug("Responding to interaction {} with pong {}",
                        interaction.id().getValueAsString(), json))
                .flatMap(response::end);
    }
}
