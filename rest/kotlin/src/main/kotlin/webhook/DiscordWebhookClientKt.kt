package io.disquark.rest.kotlin.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.json.webhook.Webhook
import io.disquark.rest.kotlin.json.webhook.EditWebhookMessageRequest
import io.disquark.rest.kotlin.json.webhook.ExecuteWebhookRequest
import io.disquark.rest.kotlin.json.webhook.ModifyWebhookWithTokenRequest
import io.disquark.rest.kotlin.request.`as`
import io.disquark.rest.kotlin.request.requestDeferred
import io.disquark.rest.response.Response
import io.disquark.rest.webhook.DiscordWebhookClient
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.replaceWithUnit

suspend fun <T : Response> DiscordWebhookClient<T>.getWebhookWithTokenSuspending(webhookId: Snowflake, webhookToken: String): Webhook =
    getWebhookWithToken(webhookId, webhookToken).awaitSuspending()

fun <T : Response> DiscordWebhookClient<T>.modifyWebhookWithToken(webhookId: Snowflake, webhookToken: String, init: ModifyWebhookWithTokenRequest.() -> Unit): Uni<Webhook> {
    return requester.requestDeferred(ModifyWebhookWithTokenRequest(requester, webhookId, webhookToken)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordWebhookClient<T>.modifyWebhookWithTokenSuspending(webhookId: Snowflake, webhookToken: String, init: ModifyWebhookWithTokenRequest.() -> Unit): Webhook =
    modifyWebhookWithToken(webhookId, webhookToken, init).awaitSuspending()

suspend fun <T : Response> DiscordWebhookClient<T>.deleteWebhookWithTokenSuspending(webhookId: Snowflake, webhookToken: String): Unit =
    deleteWebhookWithToken(webhookId, webhookToken).replaceWithUnit().awaitSuspending()

fun <T : Response> DiscordWebhookClient<T>.executeWebhook(webhookId: Snowflake, webhookToken: String, init: ExecuteWebhookRequest.() -> Unit): Uni<Message?> {
    return requester.requestDeferred(ExecuteWebhookRequest(requester, webhookId, webhookToken)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordWebhookClient<T>.executeWebhookSuspending(webhookId: Snowflake, webhookToken: String, init: ExecuteWebhookRequest.() -> Unit): Message? =
    executeWebhook(webhookId, webhookToken, init).awaitSuspending()

fun <T : Response> DiscordWebhookClient<T>.getWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Uni<Message> =
    getWebhookMessage(webhookId, webhookToken, messageId).run { threadId?.let { withThreadId(it) } ?: this }

suspend fun <T : Response> DiscordWebhookClient<T>.getWebhookMessageSuspending(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Message =
    getWebhookMessage(webhookId, webhookToken, messageId, threadId).awaitSuspending()

fun <T : Response> DiscordWebhookClient<T>.editWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, init: EditWebhookMessageRequest.() -> Unit): Uni<Message> {
    return requester.requestDeferred(EditWebhookMessageRequest(requester, webhookId, webhookToken, messageId)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordWebhookClient<T>.editWebhookMessageSuspending(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, init: EditWebhookMessageRequest.() -> Unit): Message =
    editWebhookMessage(webhookId, webhookToken, messageId, init).awaitSuspending()

fun <T : Response> DiscordWebhookClient<T>.deleteWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Uni<Unit> {
    return deleteWebhookMessage(webhookId, webhookToken, messageId)
        .run { threadId?.let { withThreadId(it) } ?: this }
        .replaceWithUnit()
}

suspend fun <T : Response> DiscordWebhookClient<T>.deleteWebhookMessageSuspending(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Unit =
    deleteWebhookMessage(webhookId, webhookToken, messageId, threadId).awaitSuspending()
