package io.disquark.rest.interactions;

import java.util.List;

import io.disquark.rest.resources.application.command.ApplicationCommand;
import io.disquark.rest.resources.interactions.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpServerResponse;

public class ApplicationCommandAutocompleteInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    public ApplicationCommandAutocompleteInteraction(
            Interaction<Interaction.ApplicationCommandData> interaction,
            HttpServerResponse response,
            DiscordInteractionsClient<?> interactionsClient) {
        super(interaction, response, interactionsClient);
    }

    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> suggestChoices(
            List<ApplicationCommand.Option.Choice> choices) {
        Interaction.Response<?> interactionResponse = Interaction.Response.builder()
                .type(Interaction.CallbackType.APPLICATION_COMMAND_AUTOCOMPLETE_RESULT)
                .data(Interaction.CallbackData.builder().choices(choices).build())
                .build();

        return serialize(interactionResponse)
                .invoke(json -> LOG.debug("Responding to interaction {} with autocomplete choices {}",
                        interaction.id().getValueAsString(), json))
                .flatMap(response::end)
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }
}