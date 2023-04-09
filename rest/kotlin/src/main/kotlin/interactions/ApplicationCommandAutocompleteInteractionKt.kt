package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ApplicationCommandAutocompleteInteraction
import io.disquark.rest.interactions.RespondedInteraction
import io.disquark.rest.json.interaction.Interaction
import io.disquark.rest.kotlin.json.command.ApplicationCommandOption
import io.smallrye.mutiny.Uni

@Suppress("UNCHECKED_CAST")
inline fun <C : ApplicationCommandOption.Choice<T>, reified T> ApplicationCommandAutocompleteInteraction.suggestChoices(init: AutocompleteCallback<C, T>.() -> Unit): Uni<RespondedInteraction<Interaction.ApplicationCommandData>> {
    val callback: AutocompleteCallback<C, T>? = when {
        T::class == String::class -> AutocompleteStringOptionCallback(this) as AutocompleteCallback<C, T>
        T::class == Int::class -> AutocompleteIntOptionCallback(this) as AutocompleteCallback<C, T>
        T::class == Double::class -> AutocompleteDoubleOptionCallback(this) as AutocompleteCallback<C, T>
        else -> null
    }

    return callback?.apply(init)?.toUni() ?: Uni.createFrom().failure(IllegalStateException())
}
