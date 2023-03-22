package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.webhook.EditWebhookMessageUni
import io.disquark.rest.kotlin.json.message.EditMessageDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class EditWebhookMessageRequest(
    private val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    private val messageId: Snowflake,
    var threadId: Snowflake? = null,
): EditMessageDsl(), Requestable {
    override fun asRequest(): Request {
        return EditWebhookMessageUni(requester, webhookId, webhookToken, messageId)
            .run { threadId?.let { withThreadId(it) } ?: this }
            .run { withContent(content.toNullableOptional()) }
            .run { withEmbeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .run { withAllowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional()) }
            .run { withComponents(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .run { withAttachments(attachments?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .asRequest()
    }
}
