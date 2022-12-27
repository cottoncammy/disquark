package org.example.rest.interactions;

import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;

public abstract class CompletableInteraction<T> {
    protected final Interaction<T> interaction;
    protected final HttpServerResponse response;
    protected final DiscordInteractionsClient<?> interactionsClient;

    CompletableInteraction(Interaction<T> interaction, HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient) {
        this.interaction = interaction;
        this.response = response;
        this.interactionsClient = interactionsClient;
    }

    public Interaction<T> getInteraction() {
        return interaction;
    }
}
