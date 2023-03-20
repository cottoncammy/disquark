package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ModalSubmitInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.smallrye.mutiny.coroutines.awaitSuspending

suspend fun ModalSubmitInteraction.respondSuspending(data: Interaction.MessageCallbackData): RespondedInteraction<Interaction.ModalSubmitData> =
    respond(data).awaitSuspending()

suspend fun ModalSubmitInteraction.deferResponseSuspending(ephemeral: Boolean): RespondedInteraction<Interaction.ModalSubmitData> =
    deferResponse(ephemeral).awaitSuspending()
