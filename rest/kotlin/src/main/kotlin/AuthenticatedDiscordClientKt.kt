package io.disquark.rest.kotlin

import io.disquark.rest.AuthenticatedDiscordClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.json.webhook.Webhook
import io.disquark.rest.kotlin.interactions.editOriginalInteractionResponse
import io.disquark.rest.kotlin.interactions.createFollowupMessage
import io.disquark.rest.kotlin.interactions.editFollowupMessage
import io.disquark.rest.kotlin.json.interaction.CreateFollowupMessage
import io.disquark.rest.kotlin.json.interaction.EditFollowupMessage
import io.disquark.rest.kotlin.json.webhook.EditWebhookMessage
import io.disquark.rest.kotlin.json.webhook.ExecuteWebhook
import io.disquark.rest.kotlin.json.webhook.ModifyWebhookWithToken
import io.disquark.rest.kotlin.webhook.deleteWebhookMessage
import io.disquark.rest.kotlin.webhook.editWebhookMessage
import io.disquark.rest.kotlin.webhook.getWebhookMessage
import io.disquark.rest.kotlin.webhook.modifyWebhookWithToken
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni

suspend fun <T : Response> AuthenticatedDiscordClient<T>.createGlobalApplicationCommand(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editGlobalApplicationCommand(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.bulkOverwriteGlobalApplicationCommands(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.createGuildApplicationCommand(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editGuildApplicationCommand(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.bulkOverwriteGuildApplicationCommands(): Unit =
    TODO()

fun <T : Response> AuthenticatedDiscordClient<T>.editOriginalInteractionResponse(applicationId: Snowflake, interactionToken: String, init: (EditOriginalInteractionResponseRequest.() -> Unit)): Uni<Message> =
    interactionsClient.editOriginalInteractionResponse(applicationId, interactionToken, init)

fun <T : Response> AuthenticatedDiscordClient<T>.createFollowupMessage(applicationId: Snowflake, interactionToken: String, init: (CreateFollowupMessage.() -> Unit)): Uni<Message> =
    interactionsClient.createFollowupMessage(applicationId, interactionToken, init)

fun <T : Response> AuthenticatedDiscordClient<T>.editFollowupMessage(applicationId: Snowflake, interactionToken: String, messageId: Snowflake, init: (EditFollowupMessage.() -> Unit)): Uni<Message> =
    interactionsClient.editFollowupMessage(applicationId, interactionToken, messageId, init)

fun <T : Response> AuthenticatedDiscordClient<T>.modifyWebhookWithToken(webhookId: Snowflake, webhookToken: String, init: ModifyWebhookWithToken.() -> Unit): Uni<Webhook> =
    webhookClient.modifyWebhookWithToken(webhookId, webhookToken, init)

fun <T : Response> AuthenticatedDiscordClient<T>.executeWebhook(webhookId: Snowflake, webhookToken: String, init: ExecuteWebhook.() -> Unit): Uni<Message?> =
    executeWebhook(webhookId, webhookToken, init)

fun <T : Response> AuthenticatedDiscordClient<T>.getWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Uni<Message> =
    webhookClient.getWebhookMessage(webhookId, webhookToken, messageId, threadId)

fun <T : Response> AuthenticatedDiscordClient<T>.editWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, init: EditWebhookMessage.() -> Unit): Uni<Message> =
    webhookClient.editWebhookMessage(webhookId, webhookToken, messageId, init)

fun <T : Response> AuthenticatedDiscordClient<T>.deleteWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Uni<Unit> =
    webhookClient.deleteWebhookMessage(webhookId, webhookToken, messageId, threadId)
