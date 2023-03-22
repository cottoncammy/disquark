package io.disquark.rest.interactions;

import io.disquark.rest.json.interaction.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;

public class ApplicationCommandInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    public ApplicationCommandInteraction(
            RoutingContext context,
            Interaction<Interaction.ApplicationCommandData> interaction,
            DiscordInteractionsClient<?> interactionsClient) {
        super(context, interaction, interactionsClient);
    }

    @Override
    public ResponseCallbackUni<Interaction.ApplicationCommandData> respond() {
        return super.respond();
    }

    @Override
    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> deferResponse(boolean ephemeral) {
        return super.deferResponse(ephemeral);
    }

    @Override
    public ModalCallbackUni<Interaction.ApplicationCommandData> popUpModal(String customId, String title) {
        return super.popUpModal(customId, title);
    }
}
