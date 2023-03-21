package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class ExecuteWebhookRequest(
    private val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    var wait: Boolean? = null,
    var threadId: Snowflake? = null,
    var username: String? = null,
    var avatarUrl: String? = null,
    var tts: Boolean? = null,
    var flags: MutableSet<Message.Flag>? = null,
    var threadName: String? = null,
): CreateMessageDsl(), Requestable {
    override fun asRequest(): Request {
        TODO("Not yet implemented")
    }
}
