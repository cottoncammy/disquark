package io.disquark.rest.kotlin.json.interaction

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.interaction.EditOriginalInteractionResponseUni
import io.disquark.rest.kotlin.json.message.EditMessageDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Requester

class EditOriginalInteractionResponse(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    private val interactionToken: String,
) : EditMessageDsl() {
    internal fun toUni(): EditOriginalInteractionResponseUni {
        return EditOriginalInteractionResponseUni.builder()
            .requester(requester)
            .applicationId(applicationId)
            .interactionToken(interactionToken)
            .content(content.toNullableOptional())
            .embeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .allowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional())
            .components(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .attachments(attachments?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .files(files)
            .build()
    }
}
