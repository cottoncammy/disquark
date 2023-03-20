package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.kotlin.request.RequestKt
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requester

class ExecuteWebhookRequest(
    override val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    var wait: Boolean? = null,
    var threadId: Snowflake? = null,
): RequestKt() {
    override fun asRequest(): Request {
        TODO("Not yet implemented")
    }
}
