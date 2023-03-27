package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.interaction.CreateFollowupMessage
import io.disquark.rest.kotlin.json.interaction.EditFollowupMessage
import io.disquark.rest.kotlin.json.interaction.EditOriginalInteractionResponse
import io.smallrye.mutiny.Uni

fun <T> RespondedInteraction<T>.editOriginalInteractionResponse(init: EditOriginalInteractionResponse.() -> Unit): Uni<Message> =
    interactionsClient.editOriginalInteractionResponse(interaction.applicationId(), interaction.token(), init)

fun <T> RespondedInteraction<T>.createFollowupMessage(init: CreateFollowupMessage.() -> Unit): Uni<Message> =
    interactionsClient.createFollowupMessage(interaction.applicationId(), interaction.token(), init)

fun <T> RespondedInteraction<T>.editFollowupMessage(messageId: Snowflake, init: EditFollowupMessage.() -> Unit): Uni<Message> =
    interactionsClient.editFollowupMessage(interaction.applicationId(), interaction.token(), messageId, init)
