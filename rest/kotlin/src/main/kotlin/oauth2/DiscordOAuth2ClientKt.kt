package io.disquark.rest.kotlin.oauth2

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.member.GuildMember
import io.disquark.rest.json.oauth2.Authorization
import io.disquark.rest.json.roleconnection.ApplicationRoleConnection
import io.disquark.rest.json.user.Connection
import io.disquark.rest.kotlin.json.roleConnection.UpdateUserApplicationRoleConnectionRequest
import io.disquark.rest.kotlin.request.`as`
import io.disquark.rest.kotlin.request.requestDeferred
import io.disquark.rest.oauth2.DiscordOAuth2Client
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

suspend fun <T : Response> DiscordOAuth2Client<T>.getCurrentUserGuildMemberSuspending(guildId: Snowflake): GuildMember =
    getCurrentUserGuildMember(guildId).awaitSuspending()

suspend fun <T : Response> DiscordOAuth2Client<T>.getUserConnectionsSuspending(): List<Connection> =
    getUserConnections().collect().asList().awaitSuspending()

suspend fun <T : Response> DiscordOAuth2Client<T>.getUserApplicationRoleConnectionSuspending(applicationId: Snowflake): ApplicationRoleConnection =
    getUserApplicationRoleConnection(applicationId).awaitSuspending()

fun <T : Response> DiscordOAuth2Client<T>.updateUserApplicationRoleConnection(applicationId: Snowflake, init: UpdateUserApplicationRoleConnectionRequest.() -> Unit): Uni<ApplicationRoleConnection> {
    return requester.requestDeferred(UpdateUserApplicationRoleConnectionRequest(requester, applicationId)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordOAuth2Client<T>.updateUserApplicationRoleConnectionSuspending(applicationId: Snowflake, init: UpdateUserApplicationRoleConnectionRequest.() -> Unit): ApplicationRoleConnection =
    updateUserApplicationRoleConnection(applicationId, init).awaitSuspending()

suspend fun <T : Response> DiscordOAuth2Client<T>.getCurrentAuthorizationInformationSuspending(): Authorization =
    getCurrentAuthorizationInformation().awaitSuspending()
