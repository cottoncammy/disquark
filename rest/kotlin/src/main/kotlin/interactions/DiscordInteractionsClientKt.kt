package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.DiscordInteractionsClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.response.Response
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.replaceWithUnit

suspend fun <T : Response> DiscordInteractionsClient<T>.getOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String): Message =
    getOriginalInteractionResponse(applicationId, interactionToken).awaitSuspending()

fun <T : Response> DiscordInteractionsClient<T>.editOriginalInteractionResponse(): Unit =
    TODO()

suspend fun <T : Response> DiscordInteractionsClient<T>.editOriginalInteractionResponseSuspending(): Unit =
    TODO()

suspend fun <T : Response> DiscordInteractionsClient<T>.deleteOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String): Unit =
    deleteOriginalInteractionResponse(applicationId, interactionToken).replaceWithUnit().awaitSuspending()

fun <T : Response> DiscordInteractionsClient<T>.createFollowupMessage(): Unit =
    TODO()

suspend fun <T : Response> DiscordInteractionsClient<T>.createFollowupMessageSuspending(): Unit =
    TODO()

suspend fun <T : Response> DiscordInteractionsClient<T>.getFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake): Message =
    getFollowupMessage(applicationId, interactionToken, messageId).awaitSuspending()

fun <T : Response> DiscordInteractionsClient<T>.editFollowupMessage(): Unit =
    TODO()

suspend fun <T : Response> DiscordInteractionsClient<T>.editFollowupMessageSuspending(): Unit =
    TODO()

suspend fun <T : Response> DiscordInteractionsClient<T>.deleteFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake): Unit =
    deleteFollowupMessage(applicationId, interactionToken, messageId).replaceWithUnit().awaitSuspending()
