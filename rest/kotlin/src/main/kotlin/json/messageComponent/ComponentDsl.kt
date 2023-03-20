package io.disquark.rest.kotlin.json.messageComponent

import io.disquark.rest.json.channel.Channel
import io.disquark.rest.json.messagecomponent.Component
import io.disquark.rest.json.messagecomponent.SelectOption

@DslMarker
annotation class ComponentDslMarker

@ComponentDslMarker
abstract class ComponentDslEntrypoint(var components: MutableList<Component>? = null) {
    private val _components: MutableList<Component>
        get() = components ?: mutableListOf()

    fun actionRow(init: ActionRow.() -> Unit) {
        _components + ActionRow().apply(init).toComponent()
    }

    fun button(style: ButtonStyle, init: (Button.() -> Unit)? = null) {
        _components + Button(style).apply{ init?.let { init() } }.toComponent()
    }

    fun selectMenu(type: SelectMenuType, init: (SelectMenu.() -> Unit)? = null) {
        _components + SelectMenu(type).apply{ init?.let { init() } }.toComponent()
    }

    fun textInput(style: TextInputStyle, value: String, init: (TextInput.() -> Unit)? = null) {
        _components + TextInput(style, value).apply{ init?.let { init() } }.toComponent()
    }
}

sealed interface ComponentDsl {
    fun toComponent(): Component
}

class ActionRow : ComponentDslEntrypoint(), ComponentDsl {
    override fun toComponent() = Component(Component.Type.ACTION_ROW).withComponents(components)
}

enum class ButtonStyle(val value: Int) {
    PRIMARY(1),
    SECONDARY(2),
    SUCCESS(3),
    DANGER(4),
    LINK(5),
}

class Button(
    val style: ButtonStyle,
    var label: String? = null,
    var emoji: PartialEmoji? = null,
    var customId: String? = null,
    var url: String? = null,
    var disabled: Boolean? = null,
): ComponentDsl {
    override fun toComponent(): Component {
        return Component(Component.Type.BUTTON)
            .withStyle(style.value)
            .run { label?.let { withLabel(label) } ?: this }
            .run { emoji?.let { withEmoji(emoji.toEmoji()) } ?: this }
            .run { customId?.let { withCustomId(customId) } ?: this }
            .run { url?.let { withUrl(url) } ?: this }
            .run { disabled?.let { withDisabled(disabled) } ?: this }
    }
}

enum class SelectMenuType(val value: Component.Type) {
    STRING(Component.Type.STRING_SELECT),
    USER(Component.Type.USER_SELECT),
    ROLE(Component.Type.STRING_SELECT),
    MENTIONABLE(Component.Type.STRING_SELECT),
    CHANNEL(Component.Type.STRING_SELECT),
}

class SelectMenu(
    val type: SelectMenuType,
    var customId: String? = null,
    var options: MutableList<SelectOption>? = null,
    var channelTypes: MutableSet<Channel.Type>? = null,
    var placeholder: String? = null,
    var minValues: Int? = null,
    var maxValues: Int? = null,
    var disabled: Boolean? = null,
): ComponentDsl {
    private val _options: MutableList<SelectOption>
        get() = options ?: mutableListOf()

    fun option(label: String, value: String, init: MutableSelectOption.() -> Unit) {
        _options + MutableSelectOption(label, value).apply(init).toImmutable()
    }

    override fun toComponent(): Component {
        return Component(type.value)
            .run { customId?.let { withCustomId(customId) } ?: this }
            .run { options?.let { withOptions(options) } ?: this }
            .run { channelTypes?.let { withChannelTypes(channelTypes) } ?: this }
            .run { placeholder?.let { withPlaceholder(placeholder) } ?: this }
            .run { minValues?.let { withMinValues(minValues) } ?: this }
            .run { maxValues?.let { withMaxValues(maxValues) } ?: this }
            .run { disabled?.let { withDisabled(disabled) } ?: this }
    }
}

enum class TextInputStyle(val value: Int) {
    SHORT(1),
    PARAGRAPH(2),
}

class TextInput(
    val style: TextInputStyle,
    val label: String,
    var customId: String? = null,
    var minLength: Int? = null,
    var maxLength: Int? = null,
    var required: Boolean? = null,
    var value: String? = null,
    var placeholder: String? = null,
): ComponentDsl {
    override fun toComponent(): Component {
        return Component(Component.Type.TEXT_INPUT)
            .withStyle(style.value)
            .withLabel(label)
            .run { customId?.let { withCustomId(customId) } ?: this }
            .run { minLength?.let { withMinLength(minLength) } ?: this }
            .run { maxLength?.let { withMaxLength(maxLength) } ?: this }
            .run { required?.let { withRequired(required) } ?: this }
            .run { value?.let { withValue(value) } ?: this }
            .run { placeholder?.let { withPlaceholder(placeholder) } ?: this }
    }
}
