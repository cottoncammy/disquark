package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ApplicationCommandAutocompleteInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.command.ApplicationCommand
import io.disquark.rest.json.interaction.Interaction
import io.smallrye.mutiny.coroutines.awaitSuspending

suspend fun ApplicationCommandAutocompleteInteraction.suggestChoicesSuspending(choices: List<ApplicationCommand.OptionChoice>): RespondedInteraction<Interaction.ApplicationCommandData> =
    suggestChoices(choices).awaitSuspending()