package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.webhook.EditWebhookMessageUni
import io.disquark.rest.kotlin.json.message.EditMessageDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Requester

class EditWebhookMessage(
    private val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    private val messageId: Snowflake,
    var threadId: Snowflake? = null,
) : EditMessageDsl() {
    internal fun toUni(): EditWebhookMessageUni {
        return EditWebhookMessageUni.builder()
            .requester(requester)
            .webhookId(webhookId)
            .webhookToken(webhookToken)
            .messageId(messageId)
            .threadId(threadId)
            .content(content.toNullableOptional())
            .embeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .allowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional())
            .components(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .attachments(attachments?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .files(files)
            .build()
    }
}
