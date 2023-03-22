package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.json.webhook.ExecuteWebhookUni
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class ExecuteWebhookRequest(
    private val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    var wait: Boolean? = null,
    var threadId: Snowflake? = null,
    var username: String? = null,
    var avatarUrl: String? = null,
    var tts: Boolean? = null,
    var flags: MutableSet<Message.Flag>? = null,
    var threadName: String? = null,
): CreateMessageDsl(), Requestable {
    override fun asRequest(): Request {
        return ExecuteWebhookUni(requester, webhookId, webhookToken)
            .run { wait?.let { withWaitForServer(it) } ?: this }
            .run { content?.let { withContent(it) } ?: this }
            .run { threadId?.let { withThreadId(it) } ?: this }
            .run { username?.let { withUsername(it) } ?: this }
            .run { avatarUrl?.let { withAvatarUrl(it) } ?: this }
            .run { tts?.let { withTts(it) } ?: this }
            .run { embeds?.let { it -> withEmbeds(it.map { it.toImmutable() }) } ?: this }
            .run { allowedMentions?.let { withAllowedMentions(it.toImmutable()) } ?: this }
            .run { components?.let { it -> withComponents(it.map { it.toImmutable() }) } ?: this }
            .run { attachments?.let { it -> withAttachments(it.map { it.toImmutable() }) } ?: this }
            .run { flags?.let { withFlags(it) } ?: this }
            .run { threadName?.let { withThreadName(it) } ?: this }
            .asRequest()
    }
}
