package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.resources.application.command.ApplicationCommand;
import org.example.rest.resources.interactions.Interaction;

import java.util.List;

public class ApplicationCommandAutocompleteInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    ApplicationCommandAutocompleteInteraction(
            Interaction<Interaction.ApplicationCommandData> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> suggestChoices(List<ApplicationCommand.Option.Choice> choices) {
        Interaction.Response<?> interactionResponse = Interaction.Response.builder()
                .type(Interaction.CallbackType.APPLICATION_COMMAND_AUTOCOMPLETE_RESULT)
                .data(Interaction.CallbackData.builder().choices(choices).build())
                .build();

        return response.end(serialize(interactionResponse)).replaceWith(new RespondedInteraction<>(interactionsClient, interaction));
    }
}
