package io.disquark.rest.kotlin.json.interaction

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.interaction.EditFollowupMessageUni
import io.disquark.rest.kotlin.json.message.EditMessageDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class EditFollowupMessageRequest(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    private val messageId: Snowflake,
): EditMessageDsl(), Requestable {
    override fun asRequest(): Request {
        return EditFollowupMessageUni(requester, applicationId, interactionToken, messageId)
            .run { content?.let { withContent(it.toNullableOptional()) } ?: this }
            .run { embeds?.let { withContent(it.toNullableOptional()) } ?: this }
            .run { allowedMentions?.let { withContent(it.toNullableOptional()) } ?: this }
            .run { components?.let { withContent(it.toNullableOptional()) } ?: this }
            .run { attachments?.let { withContent(it.toNullableOptional()) } ?: this }
            .asRequest()
    }
}