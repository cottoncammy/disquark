package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.interaction.CreateFollowupMessageRequest
import io.disquark.rest.kotlin.json.interaction.EditFollowupMessageRequest
import io.disquark.rest.kotlin.json.interaction.EditOriginalInteractionResponseRequest
import io.disquark.rest.kotlin.json.webhook.ModifyWebhookWithTokenRequest
import io.disquark.rest.kotlin.request.`as`
import io.disquark.rest.kotlin.request.requestDeferred
import io.disquark.rest.request.Requester
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.replaceWithUnit

suspend fun <T> RespondedInteraction<T>.getOriginalInteractionResponseSuspending(): Message =
    getOriginalInteractionResponse().awaitSuspending()

fun <T> RespondedInteraction<T>.editOriginalInteractionResponse(init: (EditOriginalInteractionResponseRequest.() -> Unit)): Uni<Message> =
    interactionsClient.editOriginalInteractionResponse(interaction.applicationId(), interaction.token(), init)

suspend fun <T> RespondedInteraction<T>.editOriginalInteractionResponseSuspending(init: (EditOriginalInteractionResponseRequest.() -> Unit)): Message =
    editOriginalInteractionResponse(init).awaitSuspending()

suspend fun <T> RespondedInteraction<T>.deleteOriginalInteractionResponseSuspending(): Unit =
    deleteOriginalInteractionResponse().replaceWithUnit().awaitSuspending()

fun <T> RespondedInteraction<T>.createFollowupMessage(init: CreateFollowupMessageRequest.() -> Unit): Uni<Message> =
    interactionsClient.createFollowupMessage(interaction.applicationId(), interaction.token(), init)

suspend fun <T> RespondedInteraction<T>.createFollowupMessageSuspending(init: (CreateFollowupMessageRequest.() -> Unit)): Message =
    createFollowupMessage(init).awaitSuspending()

suspend fun <T> RespondedInteraction<T>.getFollowupMessageSuspending(messageId: Snowflake): Message =
    getFollowupMessage(messageId).awaitSuspending()

fun <T> RespondedInteraction<T>.editFollowupMessage(messageId: Snowflake, init: (EditFollowupMessageRequest.() -> Unit)): Uni<Message> =
    interactionsClient.editFollowupMessage(interaction.applicationId(), interaction.token(), messageId, init)

suspend fun <T> RespondedInteraction<T>.editFollowupMessageSuspending(messageId: Snowflake, init: (EditFollowupMessageRequest.() -> Unit)): Message =
    editFollowupMessage(messageId, init).awaitSuspending()

suspend fun <T> RespondedInteraction<T>.deleteFollowupMessageSuspending(messageId: Snowflake): Unit =
    deleteFollowupMessage(messageId).replaceWithUnit().awaitSuspending()
