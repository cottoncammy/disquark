package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.CreateMessageUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.request.Requester

class CreateMessage(
    private val requester: Requester<*>,
    private val channelId: Snowflake,
    var nonce: String? = null,
    var tts: Boolean? = null,
    var messageReference: MessageReference? = null,
    var stickerIds: MutableList<Snowflake>? = null,
    var flags: MutableSet<Message.Flag>? = null,
) : CreateMessageDsl() {
    internal fun toUni(): CreateMessageUni {
        return CreateMessageUni.builder()
            .requester(requester)
            .channelId(channelId)
            .nonce(nonce)
            .tts(tts)
            .embeds(embeds?.let { it -> it.map { it.toImmutable() } })
            .allowedMentions(allowedMentions?.toImmutable())
            .messageReference(messageReference?.toImmutable())
            .components(components?.let { it -> it.map { it.toImmutable() } })
            .stickerIds(stickerIds)
            .attachments(attachments?.let { it -> it.map { it.toImmutable() } })
            .flags(flags)
            .build()
    }
}
