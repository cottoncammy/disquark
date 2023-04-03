package io.disquark.rest.kotlin.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.json.webhook.Webhook
import io.disquark.rest.kotlin.json.webhook.EditWebhookMessage
import io.disquark.rest.kotlin.json.webhook.ExecuteWebhook
import io.disquark.rest.kotlin.json.webhook.ModifyWebhookWithToken
import io.disquark.rest.response.Response
import io.disquark.rest.webhook.DiscordWebhookClient
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit

fun <T : Response> DiscordWebhookClient<T>.modifyWebhookWithToken(webhookId: Snowflake, webhookToken: String, init: ModifyWebhookWithToken.() -> Unit): Uni<Webhook> =
    ModifyWebhookWithToken(requester, webhookId, webhookToken).apply(init).toUni()

fun <T : Response> DiscordWebhookClient<T>.executeWebhook(webhookId: Snowflake, webhookToken: String, init: ExecuteWebhook.() -> Unit): Uni<Message?> =
    ExecuteWebhook(requester, webhookId, webhookToken).apply(init).toUni()

fun <T : Response> DiscordWebhookClient<T>.getWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Uni<Message> =
    getWebhookMessage(webhookId, webhookToken, messageId).run { threadId?.let { withThreadId(it) } ?: this }

fun <T : Response> DiscordWebhookClient<T>.editWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, init: EditWebhookMessage.() -> Unit): Uni<Message> =
    EditWebhookMessage(requester, webhookId, webhookToken, messageId).apply(init).toUni()

fun <T : Response> DiscordWebhookClient<T>.deleteWebhookMessage(webhookId: Snowflake, webhookToken: String, messageId: Snowflake, threadId: Snowflake? = null): Uni<Unit> {
    return deleteWebhookMessage(webhookId, webhookToken, messageId)
        .run { threadId?.let { withThreadId(it) } ?: this }
        .replaceWithUnit()
}
