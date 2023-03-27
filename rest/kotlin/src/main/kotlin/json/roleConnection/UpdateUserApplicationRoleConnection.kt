package io.disquark.rest.kotlin.json.roleConnection

import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.roleconnection.UpdateUserApplicationRoleConnectionUni
import io.disquark.rest.request.Requester

class UpdateUserApplicationRoleConnection(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    var platformName: String? = null,
    var platformUsername: String? = null,
    var metadata: Map<String, String>? = null,
) {
    internal fun toUni(): UpdateUserApplicationRoleConnectionUni {
        return UpdateUserApplicationRoleConnectionUni.builder()
            .requester(requester)
            .applicationId(applicationId)
            .platformName(platformName)
            .platformUsername(platformUsername)
            .metadata(metadata)
            .build()
    }
}
