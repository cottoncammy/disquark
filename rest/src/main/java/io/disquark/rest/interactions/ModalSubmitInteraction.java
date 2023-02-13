package io.disquark.rest.interactions;

import io.disquark.rest.resources.interactions.Interaction;
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
    public Uni<RespondedInteraction<Interaction.ModalSubmitData>> respond(Interaction.MessageCallbackData data) {
        return super.respond(data);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.ModalSubmitData>> deferResponse(boolean ephemeral) {
        return super.deferResponse(ephemeral);
    }
}
