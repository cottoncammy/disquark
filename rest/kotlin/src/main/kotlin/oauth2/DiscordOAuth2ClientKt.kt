package io.disquark.rest.kotlin.oauth2

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.member.GuildMember
import io.disquark.rest.json.oauth2.Authorization
import io.disquark.rest.json.roleconnection.ApplicationRoleConnection
import io.disquark.rest.json.roleconnection.UpdateUserApplicationRoleConnectionUni
import io.disquark.rest.json.user.Connection
import io.disquark.rest.kotlin.request.RequestKt
import io.disquark.rest.kotlin.request.`as`
import io.disquark.rest.kotlin.request.requestDeferred
import io.disquark.rest.oauth2.DiscordOAuth2Client
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requester
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

suspend fun <T : Response> DiscordOAuth2Client<T>.getCurrentUserGuildMemberSuspending(guildId: Snowflake): GuildMember =
    getCurrentUserGuildMember(guildId).awaitSuspending()

suspend fun <T : Response> DiscordOAuth2Client<T>.getUserConnectionsSuspending(): List<Connection> =
    getUserConnections().collect().asList().awaitSuspending()

suspend fun <T : Response> DiscordOAuth2Client<T>.getUserApplicationRoleConnectionSuspending(applicationId: Snowflake): ApplicationRoleConnection =
    getUserApplicationRoleConnection(applicationId).awaitSuspending()

class UpdateUserApplicationRoleConnectionRequest(
    override val requester: Requester<*>,
    private val applicationId: Snowflake,
    var platformName: String? = null,
    var platformUsername: String? = null,
    var metadata: Map<String, String>? = null,
): RequestKt() {
    override fun asRequest(): Request {
        return UpdateUserApplicationRoleConnectionUni(requester, applicationId)
            .run { platformName?.let { withPlatformName(platformName) } ?: this }
            .run { platformUsername?.let { withPlatformUsername(platformUsername) } ?: this }
            .run { metadata?.let { withMetadata(metadata) } ?: this }
            .asRequest()
    }
}

fun <T : Response> DiscordOAuth2Client<T>.updateUserApplicationRoleConnection(applicationId: Snowflake, init: (UpdateUserApplicationRoleConnectionRequest.() -> Unit)? = null): Uni<ApplicationRoleConnection> =
    requester.requestDeferred(UpdateUserApplicationRoleConnectionRequest(requester, applicationId).apply { init?.let { init() }})
        .flatMap { it.`as`() }

suspend fun <T : Response> DiscordOAuth2Client<T>.updateUserApplicationRoleConnectionSuspending(applicationId: Snowflake, init: (UpdateUserApplicationRoleConnectionRequest.() -> Unit)? = null): ApplicationRoleConnection =
    updateUserApplicationRoleConnection(applicationId, init).awaitSuspending()

suspend fun <T : Response> DiscordOAuth2Client<T>.getCurrentAuthorizationInformationSuspending(): Authorization =
    getCurrentAuthorizationInformation().awaitSuspending()
