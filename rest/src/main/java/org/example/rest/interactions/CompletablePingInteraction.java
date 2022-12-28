package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;

public class CompletablePingInteraction extends CompletableInteraction<Void> {

    CompletablePingInteraction(
            Interaction<Void> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    Uni<Void> pong() {
        Interaction.Response.create(Interaction.CallbackType.PONG);
        return response.end();
    }
}
