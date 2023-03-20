package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.replaceWithUnit

suspend fun <T> RespondedInteraction<T>.getOriginalInteractionResponseSuspending(): Message =
    getOriginalInteractionResponse().awaitSuspending()

suspend fun <T> RespondedInteraction<T>.editOriginalInteractionResponse(): Unit =
    TODO()

suspend fun <T> RespondedInteraction<T>.deleteOriginalInteractionResponseSuspending(): Unit =
    deleteOriginalInteractionResponse().replaceWithUnit().awaitSuspending()

suspend fun <T> RespondedInteraction<T>.createFollowupMessage(): Unit =
    TODO()

suspend fun <T> RespondedInteraction<T>.getFollowupMessageSuspending(messageId: Snowflake): Message =
    getFollowupMessage(messageId).awaitSuspending()

suspend fun <T> RespondedInteraction<T>.editFollowupMessage(): Unit =
    TODO()

suspend fun <T> RespondedInteraction<T>.deleteFollowupMessageSuspending(messageId: Snowflake): Unit =
    deleteFollowupMessage(messageId).replaceWithUnit().awaitSuspending()
