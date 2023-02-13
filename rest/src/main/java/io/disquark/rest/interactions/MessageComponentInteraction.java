package io.disquark.rest.interactions;

import java.util.List;

import io.disquark.rest.resources.interactions.Interaction;
import io.disquark.rest.resources.interactions.components.Component;
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
    public Uni<RespondedInteraction<Interaction.MessageComponentData>> respond(Interaction.MessageCallbackData data) {
        return super.respond(data);
    }

    @Override
    public Uni<RespondedInteraction<Interaction.MessageComponentData>> deferResponse(boolean ephemeral) {
        return super.deferResponse(ephemeral);
    }

    public Uni<RespondedInteraction<Interaction.MessageComponentData>> deferEdit() {
        return serialize(serialize(Interaction.Response.create(Interaction.CallbackType.DEFERRED_UPDATE_MESSAGE)))
                .invoke(() -> LOG.debug("Responding to interaction {} with deferred message edit",
                        interaction.id().getValueAsString()))
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    public Uni<RespondedInteraction<Interaction.MessageComponentData>> edit(Interaction.MessageCallbackData data) {
        return serialize(Interaction.Response.builder().type(Interaction.CallbackType.UPDATE_MESSAGE).data(data).build())
                .invoke(() -> LOG.debug("Responding to interaction {} with message edit",
                        interaction.id().getValueAsString()))
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    @Override
    public Uni<RespondedInteraction<Interaction.MessageComponentData>> popUpModal(String customId, String title,
            List<Component> components) {
        return super.popUpModal(customId, title, components);
    }
}
