package io.disquark.rest.kotlin.json.interaction

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.interaction.CreateFollowupMessageUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
import io.disquark.rest.request.Requester
import java.util.EnumSet

class CreateFollowupMessage(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    var tts: Boolean? = null,
    var flags: MutableSet<Message.Flag>? = null,
    var threadName: String? = null,
) : CreateMessageDsl() {
    internal fun toUni(): CreateFollowupMessageUni {
        return CreateFollowupMessageUni.builder()
            .requester(requester)
            .applicationId(applicationId)
            .interactionToken(interactionToken)
            .content(content)
            .tts(tts)
            .embeds(embeds?.let { it -> it.map { it.toImmutable() } })
            .allowedMentions(allowedMentions?.toImmutable())
            .components(components?.let { it -> it.map { it.toImmutable() } })
            .attachments(attachments?.let { it -> it.map { it.toImmutable() } })
            .flags(flags?.let { EnumSet.copyOf(it) })
            .threadName(threadName)
            .files(files)
            .build()
    }
}
