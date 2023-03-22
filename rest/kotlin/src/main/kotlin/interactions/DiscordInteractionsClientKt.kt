package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.DiscordInteractionsClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.interaction.CreateFollowupMessageRequest
import io.disquark.rest.kotlin.json.interaction.EditFollowupMessageRequest
import io.disquark.rest.kotlin.json.interaction.EditOriginalInteractionResponseRequest
import io.disquark.rest.kotlin.request.`as`
import io.disquark.rest.kotlin.request.requestDeferred
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.replaceWithUnit

suspend fun <T : Response> DiscordInteractionsClient<T>.getOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String): Message =
    getOriginalInteractionResponse(applicationId, interactionToken).awaitSuspending()

fun <T : Response> DiscordInteractionsClient<T>.editOriginalInteractionResponse(applicationId: Snowflake, interactionToken: String, init: (EditOriginalInteractionResponseRequest.() -> Unit)): Uni<Message> {
    return requester.requestDeferred(EditOriginalInteractionResponseRequest(requester, applicationId, interactionToken)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordInteractionsClient<T>.editOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String, init: (EditOriginalInteractionResponseRequest.() -> Unit)): Message =
    editOriginalInteractionResponse(applicationId, interactionToken, init).awaitSuspending()

suspend fun <T : Response> DiscordInteractionsClient<T>.deleteOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String): Unit =
    deleteOriginalInteractionResponse(applicationId, interactionToken).replaceWithUnit().awaitSuspending()

fun <T : Response> DiscordInteractionsClient<T>.createFollowupMessage(applicationId: Snowflake, interactionToken: String, init: (CreateFollowupMessageRequest.() -> Unit)): Uni<Message> {
    return requester.requestDeferred(CreateFollowupMessageRequest(requester, applicationId, interactionToken)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordInteractionsClient<T>.createFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, init: (CreateFollowupMessageRequest.() -> Unit)): Message =
    createFollowupMessage(applicationId, interactionToken, init).awaitSuspending()

suspend fun <T : Response> DiscordInteractionsClient<T>.getFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake): Message =
    getFollowupMessage(applicationId, interactionToken, messageId).awaitSuspending()

fun <T : Response> DiscordInteractionsClient<T>.editFollowupMessage(applicationId: Snowflake, interactionToken: String, messageId: Snowflake, init: (EditFollowupMessageRequest.() -> Unit)): Uni<Message> {
    return requester.requestDeferred(EditFollowupMessageRequest(requester, applicationId, interactionToken, messageId)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordInteractionsClient<T>.editFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake, init: (EditFollowupMessageRequest.() -> Unit)): Message =
    editFollowupMessage(applicationId, interactionToken, messageId, init).awaitSuspending()

suspend fun <T : Response> DiscordInteractionsClient<T>.deleteFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake): Unit =
    deleteFollowupMessage(applicationId, interactionToken, messageId).replaceWithUnit().awaitSuspending()
