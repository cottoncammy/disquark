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
        get() = _components?.getOrNull() ?: mutableListOf()

    operator fun Component.unaryPlus() {
        components + this
    }

    fun actionRow(init: ActionRow.() -> Unit) {
        +ActionRow.apply(init)
    }
}

// TODO make this internal
sealed interface Component {
    fun toImmutable(): ImmutableComponent
}

object ActionRow : ComponentDsl(), Component {
    fun button(style: ButtonStyle, init: Button.() -> Unit) {
        +Button(style).apply(init)
    }

    fun selectMenu(type: SelectMenuType, customId: String, init: SelectMenu.() -> Unit) {
        +SelectMenu(type, customId).apply(init)
    }

    fun textInput(style: TextInputStyle, customId: String, label: String, init: TextInput.() -> Unit) {
        +TextInput(style, customId, label).apply(init)
    }

    override fun toImmutable(): ImmutableComponent {
        return ImmutableComponent(ImmutableComponent.Type.ACTION_ROW)
            .run { withComponents(_components?.getOrNull()?.map { it.toImmutable() }) ?: this }
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
            .run { emoji?.let { withEmoji(it.toImmutable()) } ?: this }
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
    var customId: String,
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
        _options + this
    }

    fun option(label: String, value: String, init: (SelectOption.() -> Unit)? = null) {
        +SelectOption(label, value).apply{ init?.let { init() } }
    }

    override fun toImmutable(): ImmutableComponent {
        return ImmutableComponent(type.value)
            .withCustomId(customId)
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
    var customId: String,
    val label: String,
    var minLength: Int? = null,
    var maxLength: Int? = null,
    var required: Boolean? = null,
    var value: String? = null,
    var placeholder: String? = null,
): Component {
    override fun toImmutable(): ImmutableComponent {
        return ImmutableComponent(ImmutableComponent.Type.TEXT_INPUT)
            .withStyle(style.value)
            .withCustomId(customId)
            .withLabel(label)
            .run { minLength?.let { withMinLength(it) } ?: this }
            .run { maxLength?.let { withMaxLength(it) } ?: this }
            .run { required?.let { withRequired(it) } ?: this }
            .run { value?.let { withValue(it) } ?: this }
            .run { placeholder?.let { withPlaceholder(it) } ?: this }
    }
}
