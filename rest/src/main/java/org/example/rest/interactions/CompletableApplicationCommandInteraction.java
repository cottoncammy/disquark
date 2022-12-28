package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;

import java.util.EnumSet;
import java.util.List;

public class CompletableApplicationCommandInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    CompletableApplicationCommandInteraction(
            Interaction<Interaction.ApplicationCommandData> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> respond(Interaction.MessageCallbackData data) {
        Interaction.Response.builder().type(Interaction.CallbackType.CHANNEL_MESSAGE_WITH_SOURCE).data(data).build();
        return response.end();
    }

    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> deferResponse(boolean ephemeral) {
        Interaction.Response.Builder builder = Interaction.Response.builder().type(Interaction.CallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE);
        if (ephemeral) {
            builder.data(Interaction.CallbackData.builder().flags(EnumSet.of(Message.Flag.EPHEMERAL)).build());
        }
        return response.end();
    }

    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> popUpModal(String customId, String title, List<Component> components) {
        Interaction.Response.builder()
                .type(Interaction.CallbackType.MODAL)
                .data(Interaction.CallbackData.builder().customId(customId).title(title).components(components).build());
        return response.end();
    }
}
