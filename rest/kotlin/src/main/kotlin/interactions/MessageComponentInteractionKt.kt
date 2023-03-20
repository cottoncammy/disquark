package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.MessageComponentInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.disquark.rest.json.messagecomponent.Component
import io.smallrye.mutiny.coroutines.awaitSuspending

suspend fun MessageComponentInteraction.respondSuspending(data: Interaction.MessageCallbackData): RespondedInteraction<Interaction.MessageComponentData> =
    respond(data).awaitSuspending()

suspend fun MessageComponentInteraction.deferResponseSuspending(ephemeral: Boolean): RespondedInteraction<Interaction.MessageComponentData> =
    deferResponse(ephemeral).awaitSuspending()

suspend fun MessageComponentInteraction.deferEditSuspending(): RespondedInteraction<Interaction.MessageComponentData> =
    deferEdit().awaitSuspending()

suspend fun MessageComponentInteraction.editSuspending(data: Interaction.MessageCallbackData): RespondedInteraction<Interaction.MessageComponentData> =
    edit(data).awaitSuspending()


suspend fun MessageComponentInteraction.popupModalSuspending(customId: String, title: String, components: List<Component>): RespondedInteraction<Interaction.MessageComponentData> =
    popUpModal(customId, title, components).awaitSuspending()

