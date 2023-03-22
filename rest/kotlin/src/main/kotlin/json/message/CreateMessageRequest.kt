package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.CreateMessageUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class CreateMessageRequest(
    private val requester: Requester<*>,
    private val channelId: Snowflake,
    var nonce: String? = null,
    var tts: Boolean? = null,
    var messageReference: MessageReference? = null,
    var stickerIds: MutableList<Snowflake>? = null,
    var flags: MutableSet<Message.Flag>? = null,
): CreateMessageDsl(), Requestable {
    override fun asRequest(): Request {
        return CreateMessageUni(requester, channelId)
            .run { content?.let { withContent(it) } ?: this }
            .run { nonce?.let { withNonce(it) } ?: this }
            .run { tts?.let { withTts(it) } ?: this }
            .run { embeds?.let { it -> withEmbeds(it.map { it.toImmutable() }) } ?: this }
            .run { allowedMentions?.let { withAllowedMentions(it.toImmutable()) } ?: this }
            .run { messageReference?.let { withMessageReference(it.toImmutable()) } ?: this }
            .run { components?.let { it -> withComponents(it.map { it.toImmutable() }) } ?: this }
            .run { stickerIds?.let { withStickerIds(it) } ?: this }
            .run { attachments?.let { it -> withAttachments(it.map { it.toImmutable() }) } ?: this }
            .run { flags?.let { withFlags(it) } ?: this }
            .asRequest()
    }
}