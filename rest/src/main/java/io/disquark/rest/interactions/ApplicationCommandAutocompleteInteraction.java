package io.disquark.rest.interactions;

import java.util.List;
import java.util.function.Supplier;

import io.disquark.rest.resources.application.command.ApplicationCommand;
import io.disquark.rest.resources.interactions.Interaction;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;

public class ApplicationCommandAutocompleteInteraction extends CompletableInteraction<Interaction.ApplicationCommandData> {

    public ApplicationCommandAutocompleteInteraction(
            RoutingContext context,
            Interaction<Interaction.ApplicationCommandData> interaction,
            DiscordInteractionsClient<?> interactionsClient) {
        super(context, interaction, interactionsClient);
    }

    public Uni<RespondedInteraction<Interaction.ApplicationCommandData>> suggestChoices(
            List<ApplicationCommand.OptionChoice> choices) {
        Supplier<Interaction.Response<?>> response = () -> new Interaction.Response<>(
                Interaction.CallbackType.APPLICATION_COMMAND_AUTOCOMPLETE_RESULT)
                .withData(Interaction.CallbackData.of().withChoices(choices));

        return Uni.createFrom().deferred(() -> serialize(response.get()))
                .invoke(() -> LOG.debug("Responding to interaction {} with autocomplete choices",
                        interaction.id().getValueAsString()))
                .replaceWith(new RespondedInteraction<>(interaction, interactionsClient));
    }
}
