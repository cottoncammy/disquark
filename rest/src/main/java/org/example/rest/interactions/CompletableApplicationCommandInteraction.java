package org.example.rest.interactions;

import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;

public class CompletableApplicationCommandInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    CompletableApplicationCommandInteraction(Interaction<Interaction.ApplicationCommandData> interaction, HttpServerResponse response) {
        super(interaction, response);
    }
}
