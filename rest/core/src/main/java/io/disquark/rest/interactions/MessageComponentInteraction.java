package io.disquark.rest.interactions;

import io.disquark.rest.json.interaction.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;

public class MessageComponentInteraction extends CompletableInteraction<Interaction.MessageComponentData> {

    public MessageComponentInteraction(
            RoutingContext context,
            Interaction<Interaction.MessageComponentData> interaction,
            DiscordInteractionsClient<?> interactionsClient) {
        super(context, interaction, interactionsClient);
    }

    @Override
    public ResponseCallbackUni<Interaction.MessageComponentData> respond() {
        return super.respond();
    }

    @Override
    public Uni<RespondedInteraction<Interaction.MessageComponentData>> deferResponse(boolean ephemeral) {
        return super.deferResponse(ephemeral);
    }

    public DeferEditCallbackUni deferEdit() {
        return new DeferEditCallbackUni(context, interaction, interactionsClient);
    }

    public UpdateMessageCallbackUni edit() {
        return new UpdateMessageCallbackUni(context, interaction, interactionsClient);
    }

    @Override
    public ModalCallbackUni<Interaction.MessageComponentData> popUpModal(String customId, String title) {
        return super.popUpModal(customId, title);
    }
}
