package io.disquark.rest.interactions;

import io.disquark.rest.json.interaction.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;

public class ModalSubmitInteraction extends CompletableInteraction<Interaction.ModalSubmitData> {

    public ModalSubmitInteraction(
            RoutingContext context,
            Interaction<Interaction.ModalSubmitData> interaction,
            DiscordInteractionsClient<?> interactionsClient) {
        super(context, interaction, interactionsClient);
    }

    @Override
    public ResponseCallbackUni<Interaction.ModalSubmitData> respond() {
        return super.respond();
    }

    @Override
    public Uni<RespondedInteraction<Interaction.ModalSubmitData>> deferResponse(boolean ephemeral) {
        return super.deferResponse(ephemeral);
    }
}
