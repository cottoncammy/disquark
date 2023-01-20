package org.example.rest.interactions;

import java.util.EnumSet;
import java.util.List;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.http.HttpServerResponse;

import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CompletableInteraction<T> {
    protected static final Logger LOG = LoggerFactory.getLogger(CompletableInteraction.class);
    protected final Interaction<T> interaction;
    protected final HttpServerResponse response;
    protected final DiscordInteractionsClient<?> interactionsClient;

    CompletableInteraction(Interaction<T> interaction, HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        this.interaction = interaction;
        this.response = response;
        this.interactionsClient = interactionsClient;
    }

    protected Uni<String> serialize(Object obj) {
        return Uni.createFrom().item(Json.encode(obj));
    }

    protected Uni<RespondedInteraction<T>> respond(Interaction.MessageCallbackData data) {
        return serialize(
                Interaction.Response.builder().type(Interaction.CallbackType.CHANNEL_MESSAGE_WITH_SOURCE).data(data).build())
                .invoke(json -> LOG.debug("Responding to interaction {} with message {}",
                        interaction.id().getValueAsString(), json))
                .flatMap(response::end)
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    protected Uni<RespondedInteraction<T>> deferResponse(boolean ephemeral) {
        Interaction.Response.Builder<Interaction.CallbackData> builder = Interaction.Response.<Interaction.CallbackData> builder()
                .type(Interaction.CallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE);
        if (ephemeral) {
            builder.data(Interaction.CallbackData.builder().flags(EnumSet.of(Message.Flag.EPHEMERAL)).build());
        }

        return serialize(builder.build())
                .invoke(json -> LOG.debug("Responding to interaction with {} deferred message {}",
                        interaction.id().getValueAsString(), json))
                .flatMap(response::end)
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    protected Uni<RespondedInteraction<T>> popUpModal(String customId, String title, List<Component> components) {
        Interaction.Response<?> interactionResponse = Interaction.Response.builder()
                .type(Interaction.CallbackType.MODAL)
                .data(Interaction.CallbackData.builder().customId(customId).title(title).components(components).build())
                .build();

        return serialize(interactionResponse)
                .invoke(json -> LOG.debug("Responding to interaction {} with modal {}",
                        interaction.id().getValueAsString(), json))
                .flatMap(response::end)
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    public Interaction<T> getInteraction() {
        return interaction;
    }
}
