package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message

data class MessageReference(val messageId: Snowflake, val failIfNotExists: Boolean? = null) {
    internal fun toImmutable(): Message.Reference {
        return Message.Reference.of()
            .withMessageId(messageId)
            .run { failIfNotExists?.let { withFailIfNotExists(it) } ?: this }
    }
}