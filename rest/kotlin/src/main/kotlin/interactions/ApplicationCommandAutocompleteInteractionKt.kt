package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ApplicationCommandAutocompleteInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.disquark.rest.kotlin.json.command.ApplicationCommandOption
import io.smallrye.mutiny.Uni

inline fun <C : ApplicationCommandOption.Choice<T>, reified T> ApplicationCommandAutocompleteInteraction.suggestChoices(init: AutocompleteCallback<C, T>.() -> Unit): Uni<RespondedInteraction<Interaction.ApplicationCommandData>> {
    val callback = when {
        T::class == String::class -> AutocompleteStringOptionCallback(this)
        T::class == Int::class -> AutocompleteIntOptionCallback(this)
        T::class == Double::class -> AutocompleteDoubleOptionCallback(this)
        else -> null
    }

    return callback?.toUni() ?: Uni.createFrom().failure(IllegalStateException())
}
