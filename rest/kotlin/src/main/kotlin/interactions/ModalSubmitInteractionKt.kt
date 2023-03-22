package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.MessageComponentInteraction
import io.disquark.rest.interactions.ModalSubmitInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

fun ModalSubmitInteraction.respond(init: Any.() -> Unit): Uni<RespondedInteraction<Interaction.ModalSubmitData>> {
    TODO()
}

suspend fun ModalSubmitInteraction.respondSuspending(init: Any.() -> Unit): RespondedInteraction<Interaction.ModalSubmitData> =
    respond(init).awaitSuspending()

suspend fun ModalSubmitInteraction.deferResponseSuspending(ephemeral: Boolean): RespondedInteraction<Interaction.ModalSubmitData> =
    deferResponse(ephemeral).awaitSuspending()
