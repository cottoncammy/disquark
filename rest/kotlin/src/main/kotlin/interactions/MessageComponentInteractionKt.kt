package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.MessageComponentInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

fun MessageComponentInteraction.respond(init: ResponseCallback<MessageComponentInteraction, Interaction.MessageComponentData>.() -> Unit): Uni<RespondedInteraction<Interaction.MessageComponentData>> =
    ResponseCallback(this).apply(init).toUni()

fun MessageComponentInteraction.edit(init: UpdateMessageCallback.() -> Unit): Uni<RespondedInteraction<Interaction.MessageComponentData>> =
    UpdateMessageCallback(this).apply(init).toUni()

fun MessageComponentInteraction.popUpModal(customId: String, title: String, init: ModalCallback<MessageComponentInteraction, Interaction.MessageComponentData>.() -> Unit): Uni<RespondedInteraction<Interaction.MessageComponentData>> =
    ModalCallback(this, customId, title).apply(init).toUni()
