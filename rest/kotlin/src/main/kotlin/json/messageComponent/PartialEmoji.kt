package io.disquark.rest.kotlin.json.messageComponent

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.emoji.Emoji

data class PartialEmoji(val id: Snowflake? = null, val name: String? = null, val animated: Boolean? = null) {
    internal fun toImmutable(): Emoji {
        return Emoji.of()
            .run { id?.let { withId(it) } ?: this }
            .run { name?.let { withName(it) } ?: this }
            .run { animated?.let { withAnimated(it) } ?: this }
    }
}
