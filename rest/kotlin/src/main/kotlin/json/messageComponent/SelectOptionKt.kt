package io.disquark.rest.kotlin.json.messageComponent

import io.disquark.rest.json.messagecomponent.SelectOption

data class MutableSelectOption(
    val label: String,
    val value: String,
    var description: String? = null,
    var emoji: PartialEmoji? = null,
    var default: Boolean? = null,
) {
    fun emoji(init: PartialEmoji.() -> Unit) {
        emoji = PartialEmoji().apply(init)
    }

    fun toImmutable(): SelectOption {
        return SelectOption(label, value)
            .run { description?.let { withDescription(description) } ?: this }
            .run { emoji?.let { withEmoji(emoji.toEmoji()) } ?: this }
            .run { default?.let { withIsDefault(default) } ?: this }
    }
}