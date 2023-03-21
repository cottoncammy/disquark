package io.disquark.rest.kotlin.json.roleConnection

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.roleconnection.UpdateUserApplicationRoleConnectionUni
import io.disquark.rest.request.Request
import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester

class UpdateUserApplicationRoleConnectionRequest(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    var platformName: String? = null,
    var platformUsername: String? = null,
    var metadata: Map<String, String>? = null,
): Requestable {
    override fun asRequest(): Request {
        return UpdateUserApplicationRoleConnectionUni(requester, applicationId)
            .run { platformName?.let { withPlatformName(it) } ?: this }
            .run { platformUsername?.let { withPlatformUsername(it) } ?: this }
            .run { metadata?.let { withMetadata(it) } ?: this }
            .asRequest()
    }
}
