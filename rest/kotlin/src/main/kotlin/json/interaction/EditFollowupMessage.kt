package io.disquark.rest.kotlin.json.interaction

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.interaction.EditFollowupMessageUni
import io.disquark.rest.kotlin.json.message.EditMessageDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class EditFollowupMessage(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    private val messageId: Snowflake,
): EditMessageDsl() {
    internal fun toUni(): EditFollowupMessageUni {
        return EditFollowupMessageUni.builder()
            .requester(requester)
            .applicationId(applicationId)
            .interactionToken(interactionToken)
            .messageId(messageId)
            .content(content.toNullableOptional())
            .embeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .allowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional())
            .components(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .attachments(attachments?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .build()
    }
}