package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.EditMessageUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester
import java.util.*

class EditMessageRequest(
    private val requester: Requester<*>,
    private val channelId: Snowflake,
    private val messageId: Snowflake,
    var flags: Optional<MutableSet<Message.Flag>>? = Optional.empty(),
): EditMessageDsl(), Requestable {
    override fun asRequest(): Request {
        return EditMessageUni(requester, channelId, messageId)
            .run { withContent(content.toNullableOptional()) }
            .run { withEmbeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .run { withFlags(flags.toNullableOptional()) }
            .run { withAllowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional()) }
            .run { withComponents(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .run { withAttachments(attachments?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .asRequest()
    }
}