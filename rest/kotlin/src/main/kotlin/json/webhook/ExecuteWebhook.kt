package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.json.webhook.ExecuteWebhookUni
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
import io.disquark.rest.request.Requester

class ExecuteWebhook(
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
): CreateMessageDsl() {
    internal fun toUni(): ExecuteWebhookUni {
        return ExecuteWebhookUni.builder()
            .requester(requester)
            .webhookId(webhookId)
            .webhookToken(webhookToken)
            .apply { wait?.let { waitForServer(it) } }
            .content(content)
            .threadId(threadId)
            .username(username)
            .avatarUrl(avatarUrl)
            .tts(tts)
            .embeds(embeds?.let { it -> it.map { it.toImmutable() } })
            .allowedMentions(allowedMentions?.toImmutable())
            .components(components?.let { it -> it.map { it.toImmutable() } })
            .attachments(attachments?.let { it -> it.map { it.toImmutable() } })
            .flags(flags)
            .threadName(threadName)
            .build()
    }
}
