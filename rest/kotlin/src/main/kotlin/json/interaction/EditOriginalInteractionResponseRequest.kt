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
            .run { withContent(content.toNullableOptional()) }
            .run { withEmbeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .run { withAllowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional()) }
            .run { withComponents(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .run { withAttachments(attachments?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .asRequest()
    }
}