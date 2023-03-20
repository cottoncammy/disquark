package io.disquark.rest.kotlin

import io.disquark.rest.AuthenticatedDiscordClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.command.ApplicationCommand
import io.disquark.rest.json.command.GuildApplicationCommandPermissions
import io.disquark.rest.json.message.Message
import io.disquark.rest.json.user.User
import io.disquark.rest.json.webhook.Webhook
import io.disquark.rest.response.Response
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

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editOriginalInteractionResponse(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteOriginalInteractionResponseSuspending(applicationId: Snowflake, interactionToken: String): Unit =
    deleteOriginalInteractionResponse(applicationId, interactionToken).replaceWithUnit().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.createFollowupMessage(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake): Message =
    getFollowupMessage(applicationId, interactionToken, messageId).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editFollowupMessage(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteFollowupMessageSuspending(applicationId: Snowflake, interactionToken: String, messageId: Snowflake): Unit =
    deleteFollowupMessage(applicationId, interactionToken, messageId).replaceWithUnit().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getCurrentUserSuspending(): User =
    getCurrentUser().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getCurrentUserGuildsSuspending(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getWebhookWithTokenSuspending(webhookId: Snowflake, webhookToken: String): Webhook =
    getWebhookWithToken(webhookId, webhookToken).awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.modifyWebhookWithToken(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteWebhookWithTokenSuspending(webhookId: Snowflake, webhookToken: String): Unit =
    deleteWebhookWithToken(webhookId, webhookToken).replaceWithUnit().awaitSuspending()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.executeWebhook(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.getWebhookMessage(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.editWebhookMessage(): Unit =
    TODO()

suspend fun <T : Response> AuthenticatedDiscordClient<T>.deleteWebhookMessage(): Unit =
    TODO()
