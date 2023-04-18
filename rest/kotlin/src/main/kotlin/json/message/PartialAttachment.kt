package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.PartialAttachment as ImmutablePartialAttachment

data class PartialAttachment(val id: Snowflake, var filename: String? = null, var description: String? = null) {
    internal fun toImmutable(): ImmutablePartialAttachment {
        return ImmutablePartialAttachment(id)
            .run { filename?.let { withFilename(it) } ?: this }
            .run { description?.let { withDescription(it) } ?: this }
    }
}
