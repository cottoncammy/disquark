package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ApplicationCommandInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.disquark.rest.json.messagecomponent.Component
import io.smallrye.mutiny.coroutines.awaitSuspending

suspend fun ApplicationCommandInteraction.respondSuspending(data: Interaction.MessageCallbackData): RespondedInteraction<Interaction.ApplicationCommandData> =
    respond(data).awaitSuspending()

suspend fun ApplicationCommandInteraction.deferResponseSuspending(ephemeral: Boolean): RespondedInteraction<Interaction.ApplicationCommandData> =
    deferResponse(ephemeral).awaitSuspending()

suspend fun ApplicationCommandInteraction.popupModalSuspending(customId: String, title: String, components: List<Component>): RespondedInteraction<Interaction.ApplicationCommandData> =
    popUpModal(customId, title, components).awaitSuspending()