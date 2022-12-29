package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.Interaction;

import java.util.EnumSet;

public class CompletableModalInteraction extends CompletableInteraction<Interaction.ModalSubmitData> {

    CompletableModalInteraction(
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
