package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.MessageComponentInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.disquark.rest.json.messagecomponent.Component
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

fun MessageComponentInteraction.respond(init: Any.() -> Unit): Uni<RespondedInteraction<Interaction.MessageComponentData>> {
    TODO()
}

suspend fun MessageComponentInteraction.respondSuspending(init: Any.() -> Unit): RespondedInteraction<Interaction.MessageComponentData> =
    respond(init).awaitSuspending()

suspend fun MessageComponentInteraction.deferResponseSuspending(ephemeral: Boolean): RespondedInteraction<Interaction.MessageComponentData> =
    deferResponse(ephemeral).awaitSuspending()

suspend fun MessageComponentInteraction.deferEditSuspending(): RespondedInteraction<Interaction.MessageComponentData> =
    deferEdit().awaitSuspending()

fun MessageComponentInteraction.edit(init: Any.() -> Unit): Uni<RespondedInteraction<Interaction.MessageComponentData>> {
    TODO()
}

suspend fun MessageComponentInteraction.editSuspending(init: Any.() -> Unit): RespondedInteraction<Interaction.MessageComponentData> =
    edit(init).awaitSuspending()

fun MessageComponentInteraction.popupModal(init: Any.() -> Unit): Uni<RespondedInteraction<Interaction.MessageComponentData>> {
    TODO()
}

suspend fun MessageComponentInteraction.popupModalSuspending(init: Any.() -> Unit): RespondedInteraction<Interaction.MessageComponentData> =
    popUpModal(init).awaitSuspending()