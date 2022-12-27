package org.example.rest.interactions;

import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;

public class CompletableModalInteraction extends CompletableInteraction<Interaction.ModalSubmitData> {

    CompletableModalInteraction(Interaction<Interaction.ModalSubmitData> interaction, HttpServerResponse response) {
        super(interaction, response);
    }
}
