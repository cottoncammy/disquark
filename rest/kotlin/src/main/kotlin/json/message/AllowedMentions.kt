package io.disquark.rest.kotlin.json.message

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.AllowedMentions as ImmutableAllowedMentions

data class AllowedMentions(
    var parse: List<ImmutableAllowedMentions.Type>? = null,
    var roles: List<Snowflake>? = null,
    var users: List<Snowflake>? = null,
    var repliedUser: Boolean? = null,
) {
    internal fun toImmutable() : ImmutableAllowedMentions {
        return ImmutableAllowedMentions.of()
            .run { parse?.let { withParse(it) } ?: this }
            .run { roles?.let { withRoles(it) } ?: this }
            .run { users?.let { withUsers(it) } ?: this }
            .run { repliedUser?.let { withRepliedUser(it) } ?: this }
    }
}
