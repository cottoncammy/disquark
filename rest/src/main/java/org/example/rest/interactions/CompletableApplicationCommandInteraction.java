package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;

import java.util.List;

public class CompletableApplicationCommandInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    CompletableApplicationCommandInteraction(
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
    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> popUpModal(String customId, String title, List<Component> components) {
        return super.popUpModal(customId, title, components);
    }
}
