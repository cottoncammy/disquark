package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.webhook.ModifyWebhookWithTokenUni
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.kotlin.request.RequestKt
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requester
import io.vertx.mutiny.core.buffer.Buffer
import java.util.*

class ModifyWebhookWithTokenRequest(
    override val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    var name: String? = null,
    var avatar: Optional<Buffer>? = null,
): RequestKt() {
    override fun asRequest(): Request {
        return ModifyWebhookWithTokenUni(requester, webhookId, webhookToken)
            .run { name?.let { withName(name) } ?: this }
            .run { avatar?.let { withAvatar(avatar.toNullableOptional()) } ?: this }
            .asRequest()
    }
}
