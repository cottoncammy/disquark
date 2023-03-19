package io.disquark.rest.interactions;

import java.util.List;

import io.disquark.rest.json.interaction.Interaction;
import io.disquark.rest.json.messagecomponent.Component;
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
