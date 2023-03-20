package io.disquark.rest.kotlin.json.messageComponent

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.emoji.Emoji

data class PartialEmoji(val id: Snowflake? = null, val name: String? = null, val animated: Boolean? = null) {
    fun toEmoji(): Emoji {
        return Emoji.of()
            .run { id?.let { withId(id) } ?: this }
            .run { name?.let { withName(name) } ?: this }
            .run { animated?.let { withAnimated(animated) } ?: this }
    }
}
