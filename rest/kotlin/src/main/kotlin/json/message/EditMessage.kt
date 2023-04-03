package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.EditMessageUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.FileUpload
import io.disquark.rest.request.Requester
import java.util.*

class EditMessage(
    private val requester: Requester<*>,
    private val channelId: Snowflake,
    private val messageId: Snowflake,
    var flags: Optional<MutableSet<Message.Flag>>? = Optional.empty(),
): EditMessageDsl() {
    internal fun toUni(): EditMessageUni {
        return EditMessageUni.builder()
            .requester(requester)
            .channelId(channelId)
            .messageId(messageId)
            .content(content.toNullableOptional())
            .embeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .allowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional())
            .flags(flags)
            .components(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .attachments(attachments?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .build()
    }
}