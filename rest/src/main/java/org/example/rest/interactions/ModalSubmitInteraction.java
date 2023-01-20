package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;

import org.example.rest.resources.interactions.Interaction;

public class ModalSubmitInteraction extends CompletableInteraction<Interaction.ModalSubmitData> {

    public ModalSubmitInteraction(
            Interaction<Interaction.ModalSubmitData> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.ModalSubmitData>> respond(Interaction.MessageCallbackData data) {
        return super.respond(data);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.ModalSubmitData>> deferResponse(boolean ephemeral) {
        return super.deferResponse(ephemeral);
    }
}
