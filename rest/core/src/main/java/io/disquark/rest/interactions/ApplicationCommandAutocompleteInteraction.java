package io.disquark.rest.interactions;

import io.disquark.rest.json.interaction.Interaction;
import io.vertx.mutiny.ext.web.RoutingContext;

public class ApplicationCommandAutocompleteInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    public ApplicationCommandAutocompleteInteraction(
            RoutingContext context,
            Interaction<Interaction.ApplicationCommandData> interaction,
            DiscordInteractionsClient<?> interactionsClient) {
        super(context, interaction, interactionsClient);
    }

    public AutocompleteCallbackUni suggestChoices() {
        return new AutocompleteCallbackUni(context, interaction, interactionsClient);
    }
}
