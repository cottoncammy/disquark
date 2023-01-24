package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
public interface ModifyGuildMfaLevel extends Auditable, Requestable {

    static ModifyGuildMfaLevel create(Snowflake guildId, Guild.MfaLevel level, String auditLogReason) {
        return ImmutableModifyGuildMfaLevel.builder()
                .guildId(guildId)
                .level(level)
                .auditLogReason(Optional.ofNullable(auditLogReason))
                .build();
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

    class Response {
        private final Guild.MfaLevel level;

        @JsonCreator
        public Response(@JsonProperty("level") Guild.MfaLevel level) {
            this.level = level;
        }

        public Guild.MfaLevel getLevel() {
            return level;
        }
    }
}
