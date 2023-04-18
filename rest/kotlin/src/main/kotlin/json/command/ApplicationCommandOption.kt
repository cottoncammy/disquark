package io.disquark.rest.kotlin.json.command

import com.fasterxml.jackson.databind.node.DoubleNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.TextNode
import io.disquark.rest.json.Locale
import io.disquark.rest.json.channel.Channel
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import io.disquark.rest.json.command.ApplicationCommand.Option as ImmutableApplicationCommandOption
import io.disquark.rest.json.command.ApplicationCommand.OptionChoice as ImmutableApplicationCommandOptionChoice

open class ApplicationCommandOption(
    private val type: ImmutableApplicationCommandOption.Type,
    private val name: String,
    private val description: String,
    var required: Boolean? = null,
    var autocomplete: Boolean? = null,
) : ApplicationCommandLocalizationsDsl() {
    sealed class Choice<T>(
        protected val name: String,
        protected val value: T,
        var nameLocalizations: Optional<MutableMap<Locale, String>>? = null,
    ) {
        private val _nameLocalizations: MutableMap<Locale, String>
            get() = nameLocalizations?.getOrNull() ?: mutableMapOf<Locale, String>().also { nameLocalizations = Optional.of(it) }

        fun nameLocalizations(init: MutableMap<Locale, String>.() -> Unit) {
            _nameLocalizations + mutableMapOf<Locale, String>().apply(init)
        }

        internal abstract fun toImmutable(): ImmutableApplicationCommandOptionChoice
    }

    internal open fun toImmutable(): ImmutableApplicationCommandOption {
        return ImmutableApplicationCommandOption(type, name, description)
            .withNameLocalizations(nameLocalizations.toNullableOptional())
            .withDescriptionLocalizations(descriptionLocalizations.toNullableOptional())
            .run { required?.let { withRequired(it) } ?: this }
            .run { autocomplete?.let { withAutocomplete(it) } ?: this }
    }
}

class SubcommandGroupOption(name: String, description: String) :
    ApplicationCommandOption(ImmutableApplicationCommandOption.Type.SUB_COMMAND_GROUP, name, description),
    ApplicationCommandOptionsWithSubcommandDsl {

    override var options: MutableList<ApplicationCommandOption>? = null

    override fun toImmutable(): ImmutableApplicationCommandOption {
        return ImmutableApplicationCommandOption.copyOf(super.toImmutable())
            .run { options?.map { it.toImmutable() }?.let { withOptions(it) } ?: this }
    }
}

class SubcommandOption(name: String, description: String) :
    ApplicationCommandOption(ImmutableApplicationCommandOption.Type.SUB_COMMAND, name, description), ApplicationCommandOptionsDsl {

    override var options: MutableList<ApplicationCommandOption>? = null

    override fun toImmutable(): ImmutableApplicationCommandOption {
        return ImmutableApplicationCommandOption.copyOf(super.toImmutable())
            .run { options?.map { it.toImmutable() }?.let { withOptions(it) } ?: this }
    }
}

interface ApplicationCommandOptionChoicesDsl<C : ApplicationCommandOption.Choice<T>, T> {
    var choices: MutableList<C>?

    private val _choices: MutableList<C>
        get() = choices ?: mutableListOf<C>().also { choices = it }

    operator fun C.unaryPlus() {
        _choices + this
    }

    fun choice(name: String, value: T, init: (ApplicationCommandOption.Choice<T>.() -> Unit)? = null)
}

sealed class ApplicationCommandOptionWithChoicesDsl<C : ApplicationCommandOption.Choice<T>, T>(
    type: ImmutableApplicationCommandOption.Type,
    name: String,
    description: String,
) : ApplicationCommandOption(type, name, description), ApplicationCommandOptionChoicesDsl<C, T> {
    override var choices: MutableList<C>? = null
}

class StringOption(name: String, description: String, var minLength: Int? = null, var maxLength: Int? = null) :
    ApplicationCommandOptionWithChoicesDsl<StringOption.Choice, String>(ImmutableApplicationCommandOption.Type.STRING, name, description) {

    class Choice(name: String, value: String) : ApplicationCommandOption.Choice<String>(name, value) {
        override fun toImmutable(): ImmutableApplicationCommandOptionChoice {
            return ImmutableApplicationCommandOptionChoice(name, TextNode(value))
                .withNameLocalizations(nameLocalizations.toNullableOptional())
        }
    }

    override fun choice(name: String, value: String, init: (ApplicationCommandOption.Choice<String>.() -> Unit)?) {
        +Choice(name, value).apply { init?.let { init() } }
    }

    override fun toImmutable(): ImmutableApplicationCommandOption {
        return ImmutableApplicationCommandOption.copyOf(super.toImmutable())
            .run { choices?.map { it.toImmutable() }?.let { withChoices(it) } ?: this }
            .run { minLength?.let { withMinLength(it) } ?: this }
            .run { maxLength?.let { withMaxLength(it) } ?: this }
    }
}

sealed class NumberOption<C : ApplicationCommandOption.Choice<T>, T : Number>(
    type: ImmutableApplicationCommandOption.Type,
    name: String,
    description: String,
    var minValue: T? = null,
    var maxValue: T? = null,
) : ApplicationCommandOptionWithChoicesDsl<C, T>(type, name, description) {

    override fun toImmutable(): ImmutableApplicationCommandOption {
        return ImmutableApplicationCommandOption.copyOf(super.toImmutable())
            .run { choices?.map { it.toImmutable() }?.let { withChoices(it) } ?: this }
            .run { minValue?.let { withMinValue(it.toDouble()) } ?: this }
            .run { maxValue?.let { withMaxValue(it.toDouble()) } ?: this }
    }
}

class IntOption(name: String, description: String) : NumberOption<IntOption.Choice, Int>(ImmutableApplicationCommandOption.Type.INTEGER, name, description) {
    class Choice(name: String, value: Int) : ApplicationCommandOption.Choice<Int>(name, value) {
        override fun toImmutable(): ImmutableApplicationCommandOptionChoice {
            return ImmutableApplicationCommandOptionChoice(name, IntNode(value))
                .withNameLocalizations(nameLocalizations.toNullableOptional())
        }
    }

    override fun choice(name: String, value: Int, init: (ApplicationCommandOption.Choice<Int>.() -> Unit)?) {
        +Choice(name, value).apply { init?.let { init() } }
    }
}

class DoubleOption(name: String, description: String) : NumberOption<DoubleOption.Choice, Double>(ImmutableApplicationCommandOption.Type.NUMBER, name, description) {
    class Choice(name: String, value: Double) : ApplicationCommandOption.Choice<Double>(name, value) {
        override fun toImmutable(): ImmutableApplicationCommandOptionChoice {
            return ImmutableApplicationCommandOptionChoice(name, DoubleNode(value))
                .withNameLocalizations(nameLocalizations.toNullableOptional())
        }
    }

    override fun choice(name: String, value: Double, init: (ApplicationCommandOption.Choice<Double>.() -> Unit)?) {
        +Choice(name, value).apply { init?.let { init() } }
    }
}

class ChannelOption(name: String, description: String, var channelTypes: MutableList<Channel.Type>? = null) :
    ApplicationCommandOption(ImmutableApplicationCommandOption.Type.CHANNEL, name, description) {

    override fun toImmutable(): ImmutableApplicationCommandOption {
        return ImmutableApplicationCommandOption.copyOf(super.toImmutable())
            .run { channelTypes?.let { withChannelTypes(it) } ?: this }
    }
}
