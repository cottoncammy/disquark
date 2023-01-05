package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;

public class PingInteraction extends CompletableInteraction<Void> {

    public PingInteraction(
            Interaction<Void> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    Uni<Void> pong() {
        return response.end(serialize(Interaction.Response.create(Interaction.CallbackType.PONG)));
    }
}