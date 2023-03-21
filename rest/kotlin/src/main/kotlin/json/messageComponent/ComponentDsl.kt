package io.disquark.rest.kotlin.json.messageComponent

import io.disquark.rest.json.channel.Channel
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import io.disquark.rest.json.messagecomponent.Component as ImmutableComponent

@DslMarker
annotation class ComponentDslMarker

@ComponentDslMarker
abstract class ComponentDsl {
    protected var _components: Optional<MutableList<Component>>? = Optional.empty()

    private val components: MutableList<Component>
        get() = components?.getOrNull() ?: mutableListOf()

    operator fun Component.unaryPlus() {
        components += this
    }

    fun actionRow(init: ActionRow.() -> Unit) {
        components += ActionRow.apply(init)
    }
}

sealed interface Component {
    fun toImmutable(): ImmutableComponent
}

object ActionRow : ComponentDsl(), Component {
    fun button(style: ButtonStyle, init: (Button.() -> Unit)? = null) {
        +Button(style).apply { init?.let { init() } }
    }

    fun selectMenu(type: SelectMenuType, init: (SelectMenu.() -> Unit)? = null) {
        +SelectMenu(type).apply { init?.let { init() } }
    }

    fun textInput(style: TextInputStyle, value: String, init: (TextInput.() -> Unit)? = null) {
        +TextInput(style, value).apply { init?.let { init() } }
    }

    override fun toImmutable(): ImmutableComponent {
        return ImmutableComponent(ImmutableComponent.Type.ACTION_ROW)
            .run { withComponents(components?.getOrNull()?.map { it.build() }) ?: this }
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
): Component {
    override fun toImmutable(): ImmutableComponent {
        return ImmutableComponent(ImmutableComponent.Type.BUTTON)
            .withStyle(style.value)
            .run { label?.let { withLabel(it) } ?: this }
            .run { emoji?.let { withEmoji(it.toEmoji()) } ?: this }
            .run { customId?.let { withCustomId(it) } ?: this }
            .run { url?.let { withUrl(it) } ?: this }
            .run { disabled?.let { withDisabled(it) } ?: this }
    }
}

enum class SelectMenuType(val value: ImmutableComponent.Type) {
    STRING(ImmutableComponent.Type.STRING_SELECT),
    USER(ImmutableComponent.Type.USER_SELECT),
    ROLE(ImmutableComponent.Type.STRING_SELECT),
    MENTIONABLE(ImmutableComponent.Type.STRING_SELECT),
    CHANNEL(ImmutableComponent.Type.STRING_SELECT),
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
): Component {
    private val _options: MutableList<SelectOption>
        get() = options ?: mutableListOf()

    operator fun SelectOption.unaryPlus() {
        _options += this
    }

    fun option(label: String, value: String, init: (SelectOption.() -> Unit)? = null) {
        _options += SelectOption(label, value).apply{ init?.let { init() } }
    }

    override fun toImmutable(): ImmutableComponent {
        return ImmutableComponent(type.value)
            .run { customId?.let { withCustomId(it) } ?: this }
            .run { options?.let { it -> withOptions(it.map { it.toImmutable() }) } ?: this }
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
): Component {
    override fun toImmutable(): ImmutableComponent {
        return ImmutableComponent(ImmutableComponent.Type.TEXT_INPUT)
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
