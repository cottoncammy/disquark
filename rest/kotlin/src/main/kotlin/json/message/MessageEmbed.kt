package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.message.MessageEmbed as ImmutableMessageEmbed
import java.time.Instant

data class MessageEmbed(
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var timestamp: Instant? = null,
    var color: Int? = null,
    var footer: Footer? = null,
    var image: String? = null,
    var thumbnail: String? = null,
    var author: Author? = null,
    var fields: MutableList<Field>? = null,
) {
    data class Footer(val text: String, var iconUrl: String? = null) {
        internal fun toImmutable(): ImmutableMessageEmbed.Footer {
            return ImmutableMessageEmbed.Footer(text).run { iconUrl?.let { withIconUrl(it) } ?: this }
        }
    }

    data class Author(val name: String, var url: String? = null, var iconUrl: String? = null) {
        internal fun toImmutable(): ImmutableMessageEmbed.Author {
            return ImmutableMessageEmbed.Author(name)
                .run { url?.let { withUrl(it) } ?: this }
                .run { iconUrl?.let { withIconUrl(it) } ?: this }
        }
    }

    data class Field(val name: String, val value: String, var inline: Boolean? = null) {
        internal fun toImmutable(): ImmutableMessageEmbed.Field {
            return ImmutableMessageEmbed.Field(name, value).run { inline?.let { withInline(it) } ?: this }
        }
    }

    private val _fields: MutableList<Field>
        get() = fields ?: mutableListOf()

    operator fun Field.unaryPlus() {
        _fields + this
    }

    internal fun toImmutable(): ImmutableMessageEmbed {
        return ImmutableMessageEmbed.of()
            .run { title?.let { withTitle(it) } ?: this }
            .run { description?.let { withDescription(it) } ?: this }
            .run { url?.let { withUrl(it) } ?: this }
            .run { timestamp?.let { withTimestamp(it) } ?: this }
            .run { color?.let { withColor(it) } ?: this }
            .run { footer?.let { withFooter(it.toImmutable()) } ?: this }
            .run { image?.let { withImage(ImmutableMessageEmbed.Image.of().withUrl(it)) } ?: this }
            .run { thumbnail?.let { withThumbnail(ImmutableMessageEmbed.Thumbnail(it)) } ?: this }
            .run { author?.let { withAuthor(it.toImmutable()) } ?: this }
            .run { fields?.let { it -> withFields(it.map { it.toImmutable() }) } ?: this }
    }
}