package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.webhook.ModifyWebhookWithTokenUni
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester
import io.vertx.mutiny.core.buffer.Buffer
import java.util.*

class ModifyWebhookWithTokenRequest(
    private val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    var name: String? = null,
    var avatar: Optional<Buffer>? = Optional.empty(),
): Requestable {
    override fun asRequest(): Request {
        return ModifyWebhookWithTokenUni(requester, webhookId, webhookToken)
            .run { name?.let { withName(it) } ?: this }
            .run { withAvatar(avatar.toNullableOptional()) }
            .asRequest()
    }
}
