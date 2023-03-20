package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.AllowedMentions

data class MutableAllowedMentions(
    var parse: List<AllowedMentions.Type>? = null,
    var roles: List<Snowflake>? = null,
    var users: List<Snowflake>? = null,
    var repliedUser: Boolean? = null,
) {
    fun toImmutables() : AllowedMentions {
        return AllowedMentions.of()
    }
}
