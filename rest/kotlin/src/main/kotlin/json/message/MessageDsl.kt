package io.disquark.rest.kotlin.json.message

import io.disquark.rest.kotlin.json.messageComponent.Component
import io.disquark.rest.kotlin.json.messageComponent.ComponentDsl
import io.disquark.rest.kotlin.request.FileUploadDsl
import io.disquark.rest.request.FileUpload
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@DslMarker
annotation class MessageDslMarker

@MessageDslMarker
sealed class MessageDsl : ComponentDsl(), FileUploadDsl {
    override var files: MutableList<FileUpload> = mutableListOf()

    protected var _content: Optional<String>? = Optional.empty()
    protected var _embeds: Optional<MutableList<MessageEmbed>>? = Optional.empty()
    protected var _allowedMentions: Optional<AllowedMentions>? = Optional.empty()
    protected var _attachments: Optional<MutableList<PartialAttachment>>? = Optional.empty()

    private val embeds: MutableList<MessageEmbed>
        get() = _embeds?.getOrNull() ?: mutableListOf<MessageEmbed>().also { _embeds = Optional.of(it) }

    private val attachments: MutableList<PartialAttachment>
        get() = _attachments?.getOrNull() ?: mutableListOf<PartialAttachment>().also { _attachments = Optional.of(it) }

    operator fun MessageEmbed.unaryPlus() {
        embeds + this
    }

    fun embed(init: MessageEmbed.() -> Unit) {
        +MessageEmbed().apply(init)
    }

    fun allowedMentions(init: AllowedMentions.() -> Unit) {
        _allowedMentions = Optional.ofNullable(AllowedMentions().apply(init))
    }

    operator fun PartialAttachment.unaryPlus() {
        attachments + this
    }
}

abstract class CreateMessageDsl : MessageDsl() {
    var content: String?
        get() = _content?.getOrNull()
        set(value) {
            _content = Optional.ofNullable(value)
        }

    var embeds: MutableList<MessageEmbed>?
        get() = _embeds?.getOrNull()
        set(value) {
            _embeds = Optional.ofNullable(value)
        }

    var allowedMentions: AllowedMentions?
        get() = _allowedMentions?.getOrNull()
        set(value) {
            _allowedMentions = Optional.ofNullable(value)
        }

    var components: MutableList<Component>?
        get() = _components?.getOrNull()
        set(value) {
            _components = Optional.ofNullable(value)
        }

    var attachments: MutableList<PartialAttachment>?
        get() = _attachments?.getOrNull()
        set(value) {
            _attachments = Optional.ofNullable(value)
        }
}

abstract class EditMessageDsl : MessageDsl() {
    var content: Optional<String>?
        get() = _content
        set(value) {
            _content = value
        }

    var embeds: Optional<MutableList<MessageEmbed>>?
        get() = _embeds
        set(value) {
            _embeds = value
        }

    var allowedMentions: Optional<AllowedMentions>?
        get() = _allowedMentions
        set(value) {
            _allowedMentions = value
        }

    var components: Optional<MutableList<Component>>?
        get() = _components
        set(value) {
            _components = value
        }

    var attachments: Optional<MutableList<PartialAttachment>>?
        get() = _attachments
        set(value) {
            _attachments = value
        }
}
