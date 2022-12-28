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

    public Uni<RespondedInteraction<Interaction.ModalSubmitData>> respond(Interaction.MessageCallbackData data) {
        Interaction.Response.builder().type(Interaction.CallbackType.CHANNEL_MESSAGE_WITH_SOURCE).data(data).build();
        return response.end();
    }

    public Uni<RespondedInteraction<Interaction.ModalSubmitData>> deferResponse(boolean ephemeral) {
        Interaction.Response.Builder builder = Interaction.Response.builder().type(Interaction.CallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE);
        if (ephemeral) {
            builder.data(Interaction.CallbackData.builder().flags(EnumSet.of(Message.Flag.EPHEMERAL)).build());
        }
        return response.end();
    }
}
