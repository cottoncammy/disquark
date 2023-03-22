package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.ApplicationCommandInteraction
import io.disquark.rest.interactions.CompletableInteraction
import io.disquark.rest.interactions.MessageComponentInteraction
import io.disquark.rest.interactions.ModalSubmitInteraction
import io.disquark.rest.interactions.ResponseCallbackUni
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
import io.disquark.rest.kotlin.json.message.EditMessageDsl
import io.disquark.rest.kotlin.json.messageComponent.ComponentDsl
import java.util.*

class ResponseCallback<C : CompletableInteraction<D>, D>(
    private val interaction: C,
    var tts: Boolean? = null, var flags: MutableSet<Message.Flag>? = null,
): CreateMessageDsl() {
    internal fun toUni(): ResponseCallbackUni<D> {
        val uni = when (interaction) {
            is ApplicationCommandInteraction -> interaction.respond()
            is MessageComponentInteraction -> interaction.respond()
            is ModalSubmitInteraction -> interaction.respond()
            else -> throw IllegalStateException()
        }

        return uni.run { }
            .run { }
            .run { }
            .run { }
            .run { }
            .run { }
    }
}

class UpdateMessageCallback(
    var tts: Optional<Boolean>? = Optional.empty(),
    var flags: Optional<MutableSet<Message.Flag>>? = Optional.empty(),
): EditMessageDsl() {

}

class ModalCallback<T>(private val customId: String, private val title: String): ComponentDsl() {

}

class AutocompleteCallback {
    // TODO use abstracted application command DSL
}
