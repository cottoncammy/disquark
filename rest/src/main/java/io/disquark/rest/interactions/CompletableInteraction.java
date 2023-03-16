package io.disquark.rest.interactions;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.Interaction;
import io.disquark.rest.resources.interactions.components.Component;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CompletableInteraction<T> {
    protected static final Logger LOG = LoggerFactory.getLogger(CompletableInteraction.class);
    protected final RoutingContext context;
    protected final Interaction<T> interaction;
    protected final DiscordInteractionsClient<?> interactionsClient;

    CompletableInteraction(RoutingContext context, Interaction<T> interaction,
            DiscordInteractionsClient<?> interactionsClient) {
        this.context = context;
        this.interaction = interaction;
        this.interactionsClient = interactionsClient;
    }

    protected Uni<Void> serialize(Object obj) {
        return context.json(obj).invoke(() -> context.put("responded", true));
    }

    protected Uni<RespondedInteraction<T>> respond(Interaction.MessageCallbackData data) {
        return serialize(new Interaction.Response<>(Interaction.CallbackType.CHANNEL_MESSAGE_WITH_SOURCE).withData(data))
                .invoke(() -> LOG.debug("Responding to interaction {} with message", interaction.id().getValueAsString()))
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    protected Uni<RespondedInteraction<T>> deferResponse(boolean ephemeral) {
        Interaction.Response<Interaction.CallbackData> response = new Interaction.Response<>(
                Interaction.CallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE);
        if (ephemeral) {
            response = response.withData(Interaction.CallbackData.of().withFlags(EnumSet.of(Message.Flag.EPHEMERAL)));
        }

        return serialize(response)
                .invoke(() -> LOG.debug("Responding to interaction {} with deferred message",
                        interaction.id().getValueAsString()))
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    protected Uni<RespondedInteraction<T>> popUpModal(String customId, String title, List<Component> components) {
        Supplier<Interaction.Response<?>> response = () -> new Interaction.Response<>(Interaction.CallbackType.MODAL)
                .withData(Interaction.CallbackData.of().withCustomId(customId).withTitle(title).withComponents(components));

        return Uni.createFrom().deferred(() -> serialize(response.get()))
                .invoke(() -> LOG.debug("Responding to interaction {} with modal", interaction.id().getValueAsString()))
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }

    public Interaction<T> getInteraction() {
        return interaction;
    }
}
