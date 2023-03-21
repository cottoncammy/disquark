package io.disquark.rest.kotlin.json.messageComponent

import io.disquark.rest.json.messagecomponent.SelectOption as ImmutableSelectOption

data class SelectOption(
    val label: String,
    val value: String,
    var description: String? = null,
    var emoji: PartialEmoji? = null,
    var default: Boolean? = null,
) {
    fun toImmutable(): ImmutableSelectOption {
        return ImmutableSelectOption(label, value)
            .run { description?.let { withDescription(it) } ?: this }
            .run { emoji?.let { withEmoji(it.toEmoji()) } ?: this }
            .run { default?.let { withIsDefault(it) } ?: this }
    }
}