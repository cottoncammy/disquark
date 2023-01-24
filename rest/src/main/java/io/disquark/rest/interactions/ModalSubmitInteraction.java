package io.disquark.rest.interactions;

import io.disquark.rest.resources.interactions.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;

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
