package io.disquark.rest.kotlin.json.messageComponent

import io.disquark.rest.json.messagecomponent.SelectOption as ImmutableSelectOption

data class SelectOption(
    val label: String,
    val value: String,
    var description: String? = null,
    var emoji: PartialEmoji? = null,
    var default: Boolean? = null,
) {
    internal fun toImmutable(): ImmutableSelectOption {
        return ImmutableSelectOption(label, value)
            .run { description?.let { withDescription(it) } ?: this }
            .run { emoji?.let { withEmoji(it.toImmutable()) } ?: this }
            .run { default?.let { withIsDefault(it) } ?: this }
    }
}
