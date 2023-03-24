package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ModalSubmitInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.smallrye.mutiny.Uni

fun ModalSubmitInteraction.respond(init: ResponseCallback<ModalSubmitInteraction, Interaction.ModalSubmitData>.() -> Unit): Uni<RespondedInteraction<Interaction.ModalSubmitData>> =
    ResponseCallback(this).apply(init).toUni()
