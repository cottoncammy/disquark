package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface ModifyGuildMfaLevel extends Auditable, Requestable {

    static ModifyGuildMfaLevel create(Snowflake guildId, Guild.MfaLevel level, String auditLogReason) {
        return null;
    }

    @JsonIgnore
    Snowflake guildId();

    Guild.MfaLevel level();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/mfa"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
