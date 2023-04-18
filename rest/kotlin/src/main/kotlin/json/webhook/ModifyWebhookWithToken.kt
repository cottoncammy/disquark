package io.disquark.rest.kotlin.json.webhook

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.webhook.ModifyWebhookWithTokenUni
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Requester
import io.vertx.mutiny.core.buffer.Buffer
import java.util.*

class ModifyWebhookWithToken(
    private val requester: Requester<*>,
    private val webhookId: Snowflake,
    private val webhookToken: String,
    var name: String? = null,
    var avatar: Optional<Buffer>? = Optional.empty(),
) {
    internal fun toUni(): ModifyWebhookWithTokenUni {
        return ModifyWebhookWithTokenUni.builder()
            .requester(requester)
            .webhookId(webhookId)
            .webhookToken(webhookToken)
            .name(name)
            .avatar(avatar.toNullableOptional())
            .build()
    }
}
