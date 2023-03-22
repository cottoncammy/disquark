package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ApplicationCommandInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.disquark.rest.json.messagecomponent.Component
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

fun ApplicationCommandInteraction.respond(init: Any.() -> Unit): Uni<RespondedInteraction<Interaction.ApplicationCommandData>> {
    TODO()
}

suspend fun ApplicationCommandInteraction.respondSuspending(init: Any.() -> Unit): RespondedInteraction<Interaction.ApplicationCommandData> =
    respond(init).awaitSuspending()

suspend fun ApplicationCommandInteraction.deferResponseSuspending(ephemeral: Boolean): RespondedInteraction<Interaction.ApplicationCommandData> =
    deferResponse(ephemeral).awaitSuspending()

fun ApplicationCommandInteraction.popupModal(init: Any.() -> Unit): Uni<RespondedInteraction<Interaction.ApplicationCommandData>> {
    TODO()
}

suspend fun ApplicationCommandInteraction.popupModalSuspending(init: Any.() -> Unit): RespondedInteraction<Interaction.ApplicationCommandData> =
    popUpModal(init).awaitSuspending()