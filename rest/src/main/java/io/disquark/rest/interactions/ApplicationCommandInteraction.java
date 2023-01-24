package io.disquark.rest.interactions;

import java.util.List;

import io.disquark.rest.resources.interactions.Interaction;
import io.disquark.rest.resources.interactions.components.Component;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;

public class ApplicationCommandInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    public ApplicationCommandInteraction(
            Interaction<Interaction.ApplicationCommandData> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> respond(Interaction.MessageCallbackData data) {
        return super.respond(data);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> deferResponse(boolean ephemeral) {
        return super.deferResponse(ephemeral);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> popUpModal(String customId, String title,
            List<Component> components) {
        return super.popUpModal(customId, title, components);
    }
}
