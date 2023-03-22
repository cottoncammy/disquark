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
            .run { withContent(content.toNullableOptional()) }
            .run { withEmbeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .run { withAllowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional()) }
            .run { withComponents(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .run { withAttachments(attachments?.map { it -> it.map { it.toImmutable() } }.toNullableOptional()) }
            .asRequest()
    }
}