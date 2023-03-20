package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.AllowedMentions
import io.disquark.rest.json.message.MessageEmbed
import io.disquark.rest.json.partial.PartialAttachment
import io.disquark.rest.kotlin.json.messageComponent.ComponentDslEntrypoint

// create application command dsl, dsls for json that accepts json (startmessageinforum, modifyguildchannelpositions)

@DslMarker
annotation class CreateMessageDslMarker

@CreateMessageDslMarker
abstract class CreateMessageRequest(
    var content: String? = null,
    var embeds: MutableList<MessageEmbed>? = null,
    var allowedMentions: AllowedMentions? = null,
    var attachments: MutableList<PartialAttachment>? = null,
    var files: MutableList<Any>? = null,
): ComponentDslEntrypoint() {
    private val _embeds: MutableList<MessageEmbed>
        get() = embeds ?: mutableListOf()

    private val _attachments: MutableList<PartialAttachment>
        get() = attachments ?: mutableListOf()

    private val _files: MutableList<Any>
        get() = files ?: mutableListOf()

    fun embed(init: MessageEmbed.() -> Unit) {

    }

    fun allowedMentions(init: AllowedMentions.() -> Unit) {
        allowedMentions = AllowedMentions.of().apply(init)
    }

    fun attachment(id: Snowflake, init: (PartialAttachment.() -> Unit)? = null) {
        _attachments + PartialAttachment(id).apply { init?.let{ init() } }
    }

    fun file() {

    }
}

@DslMarker
annotation class EditMessageDslMarker

@EditMessageDslMarker
abstract class EditMessageRequest() {

}