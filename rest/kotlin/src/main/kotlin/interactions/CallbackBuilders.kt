package io.disquark.rest.kotlin.interactions

import io.disquark.rest.json.interaction.Interaction
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.message.CreateMessageDsl
import io.disquark.rest.kotlin.json.messageComponent.ComponentDsl

class CreateMessageCallbackBuilder(var tts: Boolean? = null, var flags: MutableSet<Message.Flag>? = null): CreateMessageDsl() {
    fun build(): Interaction.MessageCallbackData {
        return Interaction.MessageCallbackData.of()
    }
}

class ModalCallbackBuilder(private val customId: String, private val title: String): ComponentDsl() {
    fun build(): Interaction. {

    }
}

// TODO use abstracted application command DSL
class AutocompleteCallbackBuilder(var choices: MutableList<Any>?) {
}
