package io.disquark.rest.interactions;

import io.disquark.rest.json.interaction.Interaction;
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

    public Interaction<T> getInteraction() {
        return interaction;
    }

    protected ResponseCallbackUni<T> respond() {
        return new ResponseCallbackUni<>(context, interaction, interactionsClient);
    }

    protected Uni<RespondedInteraction<T>> deferResponse(boolean ephemeral) {
        return new DeferResponseCallbackUni<>(context, interaction, interactionsClient, ephemeral);
    }

    protected ModalCallbackUni<T> popUpModal(String customId, String title) {
        return (ModalCallbackUni<T>) Uni.createFrom().deferred(() -> new ModalCallbackUni<>(context, interaction,
                interactionsClient, customId, title));
    }
}
