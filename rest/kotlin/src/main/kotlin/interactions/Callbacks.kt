@file:Suppress("UNCHECKED_CAST")

package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ApplicationCommandAutocompleteInteraction
import io.disquark.rest.interactions.ApplicationCommandInteraction
import io.disquark.rest.interactions.CompletableInteraction
import io.disquark.rest.interactions.MessageComponentInteraction
import io.disquark.rest.interactions.ModalSubmitInteraction
import io.disquark.rest.interactions.callbacks.AutocompleteCallbackUni
import io.disquark.rest.interactions.callbacks.ModalCallbackUni
import io.disquark.rest.interactions.callbacks.ResponseCallbackUni
import io.disquark.rest.interactions.callbacks.UpdateMessageCallbackUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.command.*
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
import io.disquark.rest.kotlin.json.message.EditMessageDsl
import io.disquark.rest.kotlin.json.messageComponent.Component
import io.disquark.rest.kotlin.json.messageComponent.ComponentDsl
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import java.util.*
import kotlin.jvm.optionals.getOrNull

class ResponseCallback<C : CompletableInteraction<D>, D>(
    private val interaction: C,
    var tts: Boolean? = null,
    var flags: MutableSet<Message.Flag>? = null,
) : CreateMessageDsl() {
    internal fun toUni(): ResponseCallbackUni<D> {
        val uni: ResponseCallbackUni<D> = when (interaction) {
            is ApplicationCommandInteraction -> interaction.respond() as ResponseCallbackUni<D>
            is MessageComponentInteraction -> interaction.respond() as ResponseCallbackUni<D>
            is ModalSubmitInteraction -> interaction.respond() as ResponseCallbackUni<D>
            else -> throw IllegalStateException()
        }

        return ResponseCallbackUni.builder<D>()
            .from(uni)
            .tts(tts)
            .content(content)
            .embeds(embeds?.let { it -> it.map { it.toImmutable() } })
            .allowedMentions(allowedMentions?.toImmutable())
            .flags(flags)
            .attachments(attachments?.let { it -> it.map { it.toImmutable() } })
            .build()
    }
}

class UpdateMessageCallback(
    private val interaction: MessageComponentInteraction,
    var tts: Optional<Boolean>? = Optional.empty(),
    var flags: Optional<MutableSet<Message.Flag>>? = Optional.empty(),
) : EditMessageDsl() {
    internal fun toUni(): UpdateMessageCallbackUni {
        return UpdateMessageCallbackUni.builder()
            .from(interaction.edit())
            .tts(tts.toNullableOptional())
            .content(content.toNullableOptional())
            .embeds(embeds?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .allowedMentions(allowedMentions?.map { it.toImmutable() }.toNullableOptional())
            .flags(flags.toNullableOptional())
            .components(components?.map { it -> it.map { it.toImmutable() } }.toNullableOptional())
            .build()
    }
}

class ModalCallback<C : CompletableInteraction<D>, D>(
    private val interaction: C,
    private val customId: String,
    private val title: String,
) : ComponentDsl() {
    var components: MutableList<Component>?
        get() = _components?.getOrNull()
        set(value) {
            _components = Optional.ofNullable(value)
        }

    internal fun toUni(): ModalCallbackUni<D> {
        val uni: ModalCallbackUni<D> = when (interaction) {
            is ApplicationCommandInteraction -> interaction.popUpModal(customId, title) as ModalCallbackUni<D>
            is MessageComponentInteraction -> interaction.popUpModal(customId, title) as ModalCallbackUni<D>
            else -> throw IllegalStateException()
        }

        return ModalCallbackUni.builder<D>()
            .from(uni)
            .customId(customId)
            .title(title)
            .apply { components?.let { it -> components(it.map { it.toImmutable() }) } }
            .build()
    }
}

sealed class AutocompleteCallback<C : ApplicationCommandOption.Choice<T>, T>(private val interaction: ApplicationCommandAutocompleteInteraction) :
    ApplicationCommandOptionChoicesDsl<C, T> {

    override var choices: MutableList<C>? = null

    @PublishedApi
    internal fun toUni(): AutocompleteCallbackUni {
        return AutocompleteCallbackUni.builder()
            .from(interaction.suggestChoices())
            .apply { choices?.let { it -> choices(it.map { it.toImmutable() }) } }
            .build()
    }
}

class AutocompleteStringOptionCallback(interaction: ApplicationCommandAutocompleteInteraction) : AutocompleteCallback<StringOption.Choice, String>(interaction) {
    override fun choice(name: String, value: String, init: ApplicationCommandOption.Choice<String>.() -> Unit) {
        +StringOption.Choice(name, value).apply(init)
    }
}

class AutocompleteIntOptionCallback(interaction: ApplicationCommandAutocompleteInteraction) : AutocompleteCallback<IntOption.Choice, Int>(interaction) {
    override fun choice(name: String, value: Int, init: ApplicationCommandOption.Choice<Int>.() -> Unit) {
        +IntOption.Choice(name, value).apply(init)
    }
}

class AutocompleteDoubleOptionCallback(interaction: ApplicationCommandAutocompleteInteraction) : AutocompleteCallback<DoubleOption.Choice, Double>(interaction) {
    override fun choice(name: String, value: Double, init: ApplicationCommandOption.Choice<Double>.() -> Unit) {
        +DoubleOption.Choice(name, value).apply(init)
    }
}
