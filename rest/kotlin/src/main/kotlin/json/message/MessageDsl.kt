package io.disquark.rest.kotlin.json.message

import io.disquark.rest.kotlin.json.messageComponent.ComponentDslEntrypoint
import io.disquark.rest.kotlin.request.FileUploadDslEntrypoint

// create application command dsl, dsls for json that accepts json (startmessageinforum, modifyguildchannelpositions)

@DslMarker
annotation class CreateMessageDslMarker

@CreateMessageDslMarker
abstract class CreateMessageRequest(
    var content: String? = null,
    var embeds: MutableList<MessageEmbed>? = null,
    var allowedMentions: AllowedMentions? = null,
    var attachments: MutableList<PartialAttachment>? = null,
): ComponentDslEntrypoint() {
    private val _embeds: MutableList<MessageEmbed>
        get() = embeds ?: mutableListOf()

    private val _attachments: MutableList<PartialAttachment>
        get() = attachments ?: mutableListOf()

    operator fun MessageEmbed.unaryPlus() {
        _embeds + this
    }

    fun embed(init: MessageEmbed.() -> Unit) {
        _embeds + MessageEmbed().apply(init)
    }

    fun allowedMentions(init: AllowedMentions.() -> Unit) {
        allowedMentions = AllowedMentions().apply(init)
    }

    operator fun Collection<PartialAttachment>.unaryPlus() {
        _attachments + this
    }

    operator fun PartialAttachment.unaryPlus() {
        _attachments + this
    }

    fun file(init: FileUploadDslEntrypoint.() -> Unit) {

    }
}

@DslMarker
annotation class EditMessageDslMarker

@EditMessageDslMarker
abstract class EditMessageRequest() {

}