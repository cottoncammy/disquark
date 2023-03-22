package io.disquark.rest.kotlin

import io.disquark.rest.AuthenticatedDiscordClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.command.ApplicationCommand
import io.disquark.rest.json.command.GuildApplicationCommandPermissions
import io.disquark.rest.json.message.Message
import io.disquark.rest.json.user.User
import io.disquark.rest.json.webhook.Webhook
import io.disquark.rest.kotlin.interactions.editOriginalInteractionResponse
import io.disquark.rest.kotlin.interactions.createFollowupMessage
import io.disquark.rest.kotlin.interactions.editFollowupMessage
import io.disquark.rest.kotlin.json.interaction.CreateFollowupMessageRequest
import io.disquark.rest.kotlin.json.interaction.EditFollowupMessageRequest
import io.disquark.rest.kotlin.json.interaction.EditOriginalInteractionResponseRequest
import io.disquark.rest.kotlin.json.webhook.EditWebhookMessageRequest
import io.disquark.rest.kotlin.json.webhook.ExecuteWebhookRequest
import io.disquark.rest.kotlin.json.webhook.ModifyWebhookWithTokenRequest
import io.disquark.rest.kotlin.webhook.deleteWebhookMessage
import io.disquark.rest.kotlin.webhook.editWebhookMessage
import io.disquark.rest.kotlin.webhook.getWebhookMessage
import io.disquark.rest.kotlin.webhook.modifyWebhookWithToken
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.replaceWithUnit

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getGlobalApplicationCommandsSuspending(applicationId: Snowflake, withLocalizations: Boolean): List<ApplicationCommand> =
    getGlobalApplicationCommands(applicationId, withLocalizations).collect().asList().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.createGlobalApplicationCommand(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getGlobalApplicationCommandSuspending(applicationId: Snowflake, commandId: Snowflake): ApplicationCommand =
    getGlobalApplicationCommand(applicationId, commandId).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editGlobalApplicationCommand(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteGlobalApplicationCommandSuspending(applicationId: Snowflake, commandId: Snowflake): Unit =
    deleteGlobalApplicationCommand(applicationId, commandId).replaceWithUnit().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.bulkOverwriteGlobalApplicationCommands(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getGuildApplicationCommandsSuspending(applicationId: Snowflake, guildId: Snowflake, withLocalizations: Boolean): List<ApplicationCommand> =
    getGuildApplicationCommands(applicationId, guildId, withLocalizations).collect().asList().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.createGuildApplicationCommand(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getGuildApplicationCommandSuspending(applicationId: Snowflake, guildId: Snowflake, commandId: Snowflake): ApplicationCommand =
    getGuildApplicationCommand(applicationId, guildId, commandId).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editGuildApplicationCommand(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteGuildApplicationCommandSuspending(applicationId: Snowflake, guildId: Snowflake, commandId: Snowflake): Unit =
    deleteGuildApplicationCommand(applicationId, guildId, commandId).replaceWithUnit().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.bulkOverwriteGuildApplicationCommands(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getGuildApplicationCommandPermissionsSuspending(applicationId: Snowflake, guildId: Snowflake): List<GuildApplicationCommandPermissions> =
    getGuildApplicationCommandPermissions(applicationId, guildId).collect().asList().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getApplicationCommandPermissionsSuspending(applicationId: Snowflake, guildId: Snowflake, commandId: Snowflake): GuildApplicationCommandPermissions =
    getApplicationCommandPermissions(applicationId, guildId, commandId).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.createGlobalApplicationCommandSuspending(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String): Message =
    getOriginalInteractionResponse(applicationId, interactionToken).awaitSuspending()

fun <T : Response> AuthenticatedDiscordClient<T>.editOriginalInteractionResponse(applicationId: Snowflake, interactionToken: String, init: (EditOriginalInteractionResponseRequest.() -> Unit)): Uni<Message> =
    interactionsClient.editOriginalInteractionResponse(applicationId, interactionToken, init)

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String, init: (EditOriginalInteractionResponseRequest.() -> Unit)): Message =
    editOriginalInteractionResponse(applicationId, interactionToken, init).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String): Unit =
    deleteOriginalInteractionResponse(applicationId, interactionToken).replaceWithUnit().awaitSuspending()

fun <T : Response> AuthenticatedDiscordClient<T>.createFollowupMessage(applicationId: Snowflake, interactionToken: String, init: (CreateFollowupMessageRequest.() -> Unit)): Uni<Message> =
    interactionsClient.createFollowupMessage(applicationId, interactionToken, init)

suspend fun <T : Response> AuthenticatedDiscordClient<T>.createFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, init: (CreateFollowupMessageRequest.() -> Unit)): Message =
    createFollowupMessage(applicationId, interactionToken, init).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake): Message =
    getFollowupMessage(applicationId, interactionToken, messageId).awaitSuspending()

fun <T : Response> AuthenticatedDiscordClient<T>.editFollowupMessage(applicationId: Snowflake, interactionToken: String, messageId: Snowflake, init: (EditFollowupMessageRequest.() -> Unit)): Uni<Message> =
    interactionsClient.editFollowupMessage(applicationId, interactionToken, messageId, init)

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake, init: (EditFollowupMessageRequest.() -> Unit)): Message =
    editFollowupMessage(applicationId, interactionToken, messageId, init).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake): Unit =
    deleteFollowupMessage(applicationId, interactionToken, messageId).replaceWithUnit().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getCurrentUserSuspending(): User =
    getCurrentUser().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getCurrentUserGuildsSuspending(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getWebhookWithTokenSuspending(webhookId: Snowflake, webhookToken: String): Webhook =
    getWebhookWithToken(webhookId, webhookToken).awaitSuspending()

fun <T : Response> AuthenticatedDiscordClient<T>.modifyWebhookWithToken(webhookId: Snowflake, webhookToken: String, init: ModifyWebhookWithTokenRequest.() -> Unit): Uni<Webhook> =
    webhookClient.modifyWebhookWithToken(webhookId, webhookToken, init)

suspend fun <T : Response> AuthenticatedDiscordClient<T>.modifyWebhookWithTokenSuspending(webhookId: Snowflake, webhookToken: String, init: ModifyWebhookWithTokenRequest.() -> Unit): Webhook =
    modifyWebhookWithToken(webhookId, webhookToken, init).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteWebhookWithTokenSuspending(webhookId: Snowflake, webhookToken: String): Unit =
    deleteWebhookWithToken(webhookId, webhookToken).replaceWithUnit().awaitSuspending()

fun <T : Response> AuthenticatedDiscordClient<T>.executeWebhook(webhookId: Snowflake, webhookToken: String, init: ExecuteWebhookRequest.() -> Unit): Uni<Message?> =
    executeWebhook(webhookId, webhookToken, init)

suspend fun <T : Response> AuthenticatedDiscordClient<T>.executeWebhookSuspending(webhookId: Snowflake, webhookToken: String, init: ExecuteWebhookRequest.() -> Unit): Message? =
    executeWebhook(webhookId, webhookToken, init).awaitSuspending()

fun <T : Response> AuthenticatedDiscordClient<T>.getWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Uni<Message> =
    webhookClient.getWebhookMessage(webhookId, webhookToken, messageId, threadId)

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getWebhookMessageSuspending(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Message =
    getWebhookMessage(webhookId, webhookToken, messageId, threadId).awaitSuspending()

fun <T : Response> AuthenticatedDiscordClient<T>.editWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, init: EditWebhookMessageRequest.() -> Unit): Uni<Message> =
    webhookClient.editWebhookMessage(webhookId, webhookToken, messageId, init)

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editWebhookMessageSuspending(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, init: EditWebhookMessageRequest.() -> Unit): Message =
    editWebhookMessage(webhookId, webhookToken, messageId, init).awaitSuspending()

fun <T : Response> AuthenticatedDiscordClient<T>.deleteWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Uni<Unit> =
    webhookClient.deleteWebhookMessage(webhookId, webhookToken, messageId, threadId)

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteWebhookMessageSuspending(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Unit =
    deleteWebhookMessage(webhookId, webhookToken, messageId, threadId).awaitSuspending()
