package io.disquark.rest.kotlin

import io.disquark.rest.AuthenticatedDiscordClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.command.ApplicationCommand
import io.disquark.rest.json.message.Message
import io.disquark.rest.json.webhook.Webhook
import io.disquark.rest.kotlin.interactions.createFollowupMessage
import io.disquark.rest.kotlin.interactions.editFollowupMessage
import io.disquark.rest.kotlin.interactions.editOriginalInteractionResponse
import io.disquark.rest.kotlin.json.command.BulkOverwriteGlobalApplicationCommands
import io.disquark.rest.kotlin.json.command.BulkOverwriteGuildApplicationCommands
import io.disquark.rest.kotlin.json.command.CreateGlobalChatInputCommand
import io.disquark.rest.kotlin.json.command.CreateGlobalMessageCommand
import io.disquark.rest.kotlin.json.command.CreateGlobalUserCommand
import io.disquark.rest.kotlin.json.command.CreateGuildChatInputCommand
import io.disquark.rest.kotlin.json.command.CreateGuildMessageCommand
import io.disquark.rest.kotlin.json.command.CreateGuildUserCommand
import io.disquark.rest.kotlin.json.command.EditGlobalApplicationCommand
import io.disquark.rest.kotlin.json.command.EditGuildApplicationCommand
import io.disquark.rest.kotlin.json.interaction.CreateFollowupMessage
import io.disquark.rest.kotlin.json.interaction.EditFollowupMessage
import io.disquark.rest.kotlin.json.interaction.EditOriginalInteractionResponse
import io.disquark.rest.kotlin.json.webhook.EditWebhookMessage
import io.disquark.rest.kotlin.json.webhook.ExecuteWebhook
import io.disquark.rest.kotlin.json.webhook.ModifyWebhookWithToken
import io.disquark.rest.kotlin.webhook.deleteWebhookMessage
import io.disquark.rest.kotlin.webhook.editWebhookMessage
import io.disquark.rest.kotlin.webhook.getWebhookMessage
import io.disquark.rest.kotlin.webhook.modifyWebhookWithToken
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni

fun <T : Response> AuthenticatedDiscordClient<T>.createGlobalChatInputCommand(applicationId: Snowflake, name: String, init: CreateGlobalChatInputCommand.() -> Unit): Uni<ApplicationCommand> =
    CreateGlobalChatInputCommand(requester, applicationId, name).apply(init).toUni()

fun <T : Response> AuthenticatedDiscordClient<T>.createGlobalUserCommand(applicationId: Snowflake, name: String, init: CreateGlobalUserCommand.() -> Unit): Uni<ApplicationCommand> =
    CreateGlobalUserCommand(requester, applicationId, name).apply(init).toUni()

fun <T : Response> AuthenticatedDiscordClient<T>.createGlobalMessageCommand(applicationId: Snowflake, name: String, init: CreateGlobalMessageCommand.() -> Unit): Uni<ApplicationCommand> =
    CreateGlobalMessageCommand(requester, applicationId, name).apply(init).toUni()

fun <T : Response> AuthenticatedDiscordClient<T>.editGlobalApplicationCommand(applicationId: Snowflake, commandId: Snowflake, init: EditGlobalApplicationCommand.() -> Unit): Uni<ApplicationCommand> =
    EditGlobalApplicationCommand(requester, applicationId, commandId).apply(init).toUni()

fun <T : Response> AuthenticatedDiscordClient<T>.bulkOverwriteGlobalApplicationCommands(applicationId: Snowflake, init: BulkOverwriteGlobalApplicationCommands.() -> Unit): Multi<ApplicationCommand> =
    BulkOverwriteGlobalApplicationCommands(requester, applicationId).apply(init).toMulti()

fun <T : Response> AuthenticatedDiscordClient<T>.createGuildChatInputCommand(applicationId: Snowflake, guildId: Snowflake, name: String, init: CreateGuildChatInputCommand.() -> Unit): Uni<ApplicationCommand> =
    CreateGuildChatInputCommand(requester, applicationId, guildId, name).apply(init).toUni()

fun <T : Response> AuthenticatedDiscordClient<T>.createGuildUserCommand(applicationId: Snowflake, guildId: Snowflake, name: String, init: CreateGuildUserCommand.() -> Unit): Uni<ApplicationCommand> =
    CreateGuildUserCommand(requester, applicationId, guildId, name).apply(init).toUni()

fun <T : Response> AuthenticatedDiscordClient<T>.createGuildMessageCommand(applicationId: Snowflake, guildId: Snowflake, name: String, init: CreateGuildMessageCommand.() -> Unit): Uni<ApplicationCommand> =
    CreateGuildMessageCommand(requester, applicationId, guildId, name).apply(init).toUni()

fun <T : Response> AuthenticatedDiscordClient<T>.editGuildApplicationCommand(applicationId: Snowflake, guildId: Snowflake, commandId: Snowflake, init: EditGuildApplicationCommand.() -> Unit): Uni<ApplicationCommand> =
    EditGuildApplicationCommand(requester, applicationId, guildId, commandId).apply(init).toUni()

fun <T : Response> AuthenticatedDiscordClient<T>.bulkOverwriteGuildApplicationCommands(applicationId: Snowflake, guildId: Snowflake, init: BulkOverwriteGuildApplicationCommands.() -> Unit): Multi<ApplicationCommand> =
    BulkOverwriteGuildApplicationCommands(requester, applicationId, guildId).apply(init).toMulti()

fun <T : Response> AuthenticatedDiscordClient<T>.editOriginalInteractionResponse(applicationId: Snowflake, interactionToken: String, init: EditOriginalInteractionResponse.() -> Unit): Uni<Message> =
    interactionsClient.editOriginalInteractionResponse(applicationId, interactionToken, init)

fun <T : Response> AuthenticatedDiscordClient<T>.createFollowupMessage(applicationId: Snowflake, interactionToken: String, init: CreateFollowupMessage.() -> Unit): Uni<Message> =
    interactionsClient.createFollowupMessage(applicationId, interactionToken, init)

fun <T : Response> AuthenticatedDiscordClient<T>.editFollowupMessage(applicationId: Snowflake, interactionToken: String, messageId: Snowflake, init: EditFollowupMessage.() -> Unit): Uni<Message> =
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
