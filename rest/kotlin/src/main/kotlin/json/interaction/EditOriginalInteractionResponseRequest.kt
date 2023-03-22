package io.disquark.rest.kotlin.json.interaction

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.interaction.EditOriginalInteractionResponseUni
import io.disquark.rest.kotlin.json.message.EditMessageDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class EditOriginalInteractionResponseRequest(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    private val interactionToken: String,
): EditMessageDsl(), Requestable {
    override fun asRequest(): Request {
        return EditOriginalInteractionResponseUni(requester, applicationId, interactionToken)
            .run { content?.let { withContent(it.toNullableOptional()) } ?: this }
            .run { embeds?.let { withContent(it.toNullableOptional()) } ?: this }
            .run { allowedMentions?.let { withContent(it.toNullableOptional()) } ?: this }
            .run { components?.let { withContent(it.toNullableOptional()) } ?: this }
            .run { attachments?.let { withContent(it.toNullableOptional()) } ?: this }
            .asRequest()
    }
}