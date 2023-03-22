package io.disquark.rest.kotlin.json.interaction

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.interaction.CreateFollowupMessageUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class CreateFollowupMessageRequest(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    var tts: Boolean? = null,
    var flags: MutableSet<Message.Flag>? = null,
    var threadName: String? = null,
): CreateMessageDsl(), Requestable {
    override fun asRequest(): Request {
        return CreateFollowupMessageUni(requester, applicationId, interactionToken)
            .run { content?.let { withContent(it) } ?: this }
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