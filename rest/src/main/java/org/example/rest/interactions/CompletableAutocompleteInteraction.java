package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.interactions.Interaction;

public class CompletableAutocompleteInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    CompletableAutocompleteInteraction(Interaction<Interaction.ApplicationCommandData> interaction, HttpServerResponse response) {
        super(interaction, response);
    }

    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> suggestChoices() {

    }
}
