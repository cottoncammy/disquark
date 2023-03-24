package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ApplicationCommandInteraction
import io.disquark.rest.interactions.ModalSubmitInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

fun ApplicationCommandInteraction.respond(init: ResponseCallback<ApplicationCommandInteraction, Interaction.ApplicationCommandData>.() -> Unit): Uni<RespondedInteraction<Interaction.ApplicationCommandData>> =
    ResponseCallback(this).apply(init).toUni()

fun ApplicationCommandInteraction.popUpModal(customId: String, title: String, init: ModalCallback<ApplicationCommandInteraction, Interaction.ApplicationCommandData>.() -> Unit): Uni<RespondedInteraction<Interaction.ApplicationCommandData>> =
    ModalCallback(this, customId, title).apply(init).toUni()
