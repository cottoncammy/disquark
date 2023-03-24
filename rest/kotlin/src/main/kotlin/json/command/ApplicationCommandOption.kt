package io.disquark.rest.kotlin.json.command

import io.disquark.rest.json.Locale
import io.disquark.rest.json.channel.Channel
import java.util.Optional
import io.disquark.rest.json.command.ApplicationCommand.Option as ImmutableApplicationCommandOption

data class ApplicationCommandOption(
    val type: ImmutableApplicationCommandOption.Type,
    val name: String,
    var nameLocalizations: Optional<MutableMap<Locale, String>>? = Optional.empty(),
    val description: String,
    var descriptionLocalizations: Optional<MutableMap<Locale, String>>? = Optional.empty(),
    var required: Boolean? = null,
    var choices: MutableList<Any>? = null,
    var options: MutableList<ApplicationCommandOption>? = null,
    var channelTypes: MutableSet<Channel.Type>? = null,
    var minValue: Int? = null,
    var maxValue: Int? = null,
    var minLength: Int? = null,
    var maxLength: Int? = null,
    var autocomplete: Boolean? = null,
): ApplicationCommandLocalizationsDsl(), ApplicationCommandOptionsDsl {
    data class Choice() {
        fun toImmutable(): Any {

        }
    }

    fun choices() {

    }

    fun toImmutable(): ImmutableApplicationCommandOption {

    }
}