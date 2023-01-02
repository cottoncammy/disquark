package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;

import java.util.EnumSet;
import java.util.List;

public abstract class CompletableInteraction<T> {
    protected final Interaction<T> interaction;
    protected final HttpServerResponse response;
    protected final DiscordInteractionsClient<?> interactionsClient;

    CompletableInteraction(Interaction<T> interaction, HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient) {
        this.interaction = interaction;
        this.response = response;
        this.interactionsClient = interactionsClient;
    }

    protected String serialize(Object obj) {
        return Json.encode(obj);
    }

    protected Uni<RespondedInteraction<T>> respond(Interaction.MessageCallbackData data) {
        return response.end(serialize(Interaction.Response.builder().type(Interaction.CallbackType.CHANNEL_MESSAGE_WITH_SOURCE).data(data).build()))
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    protected Uni<RespondedInteraction<T>> deferResponse(boolean ephemeral) {
        Interaction.Response.Builder<Interaction.CallbackData> builder = Interaction.Response.<Interaction.CallbackData>builder().type(Interaction.CallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE);
        if (ephemeral) {
            builder.data(Interaction.CallbackData.builder().flags(EnumSet.of(Message.Flag.EPHEMERAL)).build());
        }
        return response.end(serialize(builder.build())).replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    protected Uni<RespondedInteraction<T>> popUpModal(String customId, String title, List<Component> components) {
        Interaction.Response<?> interactionResponse = Interaction.Response.builder()
                .type(Interaction.CallbackType.MODAL)
                .data(Interaction.CallbackData.builder().customId(customId).title(title).components(components).build())
                .build();

        return response.end(serialize(interactionResponse)).replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    public Interaction<T> getInteraction() {
        return interaction;
    }
}
