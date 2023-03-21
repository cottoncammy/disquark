package io.disquark.rest.kotlin.json.messageComponent

import io.disquark.rest.json.channel.Channel
import io.disquark.rest.json.messagecomponent.Component

@DslMarker
annotation class ComponentDslMarker

@ComponentDslMarker
abstract class ComponentDslEntrypoint(var components: MutableList<out ComponentDsl>? = null) {
    protected val _components: MutableList<out ComponentDsl>
        get() = components ?: mutableListOf()

    operator fun ComponentDsl.unaryPlus() {
        _components + this
    }

    fun actionRow(init: ActionRow.() -> Unit) {
        _components + ActionRow().apply(init)
    }
}

sealed interface ComponentDsl {
    fun build(): Component
}

class ActionRow : ComponentDslEntrypoint(), ComponentDsl {
    fun button(style: ButtonStyle, init: (Button.() -> Unit)? = null) {
        _components + Button(style).apply{ init?.let { init() } }
    }

    fun selectMenu(type: SelectMenuType, init: (SelectMenu.() -> Unit)? = null) {
        _components + SelectMenu(type).apply{ init?.let { init() } }
    }

    fun textInput(style: TextInputStyle, value: String, init: (TextInput.() -> Unit)? = null) {
        _components + TextInput(style, value).apply{ init?.let { init() } }
    }

    override fun build(): Component {
        return Component(Component.Type.ACTION_ROW)
            .run{ components?.let { it -> withComponents(it.map { it.build() }) } ?: this }
    }
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
    override fun build(): Component {
        return Component(Component.Type.BUTTON)
            .withStyle(style.value)
            .run { label?.let { withLabel(it) } ?: this }
            .run { emoji?.let { withEmoji(it.toEmoji()) } ?: this }
            .run { customId?.let { withCustomId(it) } ?: this }
            .run { url?.let { withUrl(it) } ?: this }
            .run { disabled?.let { withDisabled(it) } ?: this }
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

    operator fun SelectOption.unaryPlus() {
        _options + this
    }

    fun option(label: String, value: String, init: (SelectOption.() -> Unit)? = null) {
        _options + SelectOption(label, value).apply{ init?.let { init() } }.toImmutable()
    }

    override fun build(): Component {
        return Component(type.value)
            .run { customId?.let { withCustomId(it) } ?: this }
            .run { options?.let { withOptions(it) } ?: this }
            .run { channelTypes?.let { withChannelTypes(it) } ?: this }
            .run { placeholder?.let { withPlaceholder(it) } ?: this }
            .run { minValues?.let { withMinValues(it) } ?: this }
            .run { maxValues?.let { withMaxValues(it) } ?: this }
            .run { disabled?.let { withDisabled(it) } ?: this }
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
    override fun build(): Component {
        return Component(Component.Type.TEXT_INPUT)
            .withStyle(style.value)
            .withLabel(label)
            .run { customId?.let { withCustomId(it) } ?: this }
            .run { minLength?.let { withMinLength(it) } ?: this }
            .run { maxLength?.let { withMaxLength(it) } ?: this }
            .run { required?.let { withRequired(it) } ?: this }
            .run { value?.let { withValue(it) } ?: this }
            .run { placeholder?.let { withPlaceholder(it) } ?: this }
    }
}
