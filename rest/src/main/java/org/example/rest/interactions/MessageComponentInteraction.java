package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;

import java.util.List;

public class MessageComponentInteraction extends CompletableInteraction<Interaction.MessageComponentData> {

    MessageComponentInteraction(
            Interaction<Interaction.MessageComponentData> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.MessageComponentData>> respond(Interaction.MessageCallbackData data) {
        return super.respond(data);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.MessageComponentData>> deferResponse(boolean ephemeral) {
        return super.deferResponse(ephemeral);
    }

    public Uni<RespondedInteraction<Interaction.MessageComponentData>> deferEdit() {
        return response.end(serialize(Interaction.Response.create(Interaction.CallbackType.DEFERRED_UPDATE_MESSAGE)))
                .replaceWith(new RespondedInteraction<>(interactionsClient, interaction));
    }

    public Uni<RespondedInteraction<Interaction.MessageComponentData>> edit(Interaction.MessageCallbackData data) {
        return response.end(serialize(Interaction.Response.builder().type(Interaction.CallbackType.UPDATE_MESSAGE).data(data).build()))
                .replaceWith(new RespondedInteraction<>(interactionsClient, interaction));
    }

    @Override
    public Uni<RespondedInteraction<Interaction.MessageComponentData>> popUpModal(String customId, String title, List<Component> components) {
        return super.popUpModal(customId, title, components);
    }
}
