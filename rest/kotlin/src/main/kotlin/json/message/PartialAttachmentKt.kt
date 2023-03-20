package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.PartialAttachment
import kotlin.jvm.optionals.getOrNull

val PartialAttachment.id: Snowflake
    get() = id()

val PartialAttachment.filename: String?
    get() = filename().getOrNull()

val PartialAttachment.description: String?
    get() = description().getOrNull()
