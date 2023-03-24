package io.disquark.rest.kotlin.oauth2

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.member.GuildMember
import io.disquark.rest.json.oauth2.Authorization
import io.disquark.rest.json.roleconnection.ApplicationRoleConnection
import io.disquark.rest.json.user.Connection
import io.disquark.rest.kotlin.json.roleConnection.UpdateUserApplicationRoleConnection
import io.disquark.rest.oauth2.DiscordOAuth2Client
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

fun <T : Response> DiscordOAuth2Client<T>.updateUserApplicationRoleConnection(applicationId: Snowflake, init: UpdateUserApplicationRoleConnection.() -> Unit): Uni<ApplicationRoleConnection> =
    UpdateUserApplicationRoleConnection(requester, applicationId).apply(init).toUni()
