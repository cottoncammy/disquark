package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.AllowedMentions
import io.disquark.rest.json.message.MessageEmbed
import io.disquark.rest.json.messagecomponent.Component
import io.disquark.rest.json.partial.PartialAttachment
import io.disquark.rest.kotlin.request.RequestKt
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requester

class EditWebhookMessageRequest(
    override val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    private val messageId: Snowflake,
    var threadId: Snowflake? = null,
    var content: String? = null,
    var embeds: MutableList<MessageEmbed>? = null,
    var allowedMentions: AllowedMentions? = null,
    var components: MutableList<Component>? = null,
    var attachments: MutableList<PartialAttachment>? = null,
): RequestKt() {
    override fun asRequest(): Request {
        TODO("Not yet implemented")
    }
}
