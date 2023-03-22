package io.disquark.rest.kotlin.json.interaction

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.interaction.CreateFollowupMessageUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
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
            .asRequest()
    }
}