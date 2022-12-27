package org.example.rest.interactions;

import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;

public class CompletableComponentInteraction extends CompletableInteraction<Interaction.MessageComponentData> {

    CompletableComponentInteraction(Interaction<Interaction.MessageComponentData> interaction, HttpServerResponse response) {
        super(interaction, response);
    }
}
