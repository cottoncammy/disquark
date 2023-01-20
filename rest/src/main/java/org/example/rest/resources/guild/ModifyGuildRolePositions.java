package org.example.rest.resources.guild;

import static org.example.rest.util.Variables.variables;

import java.util.List;
import java.util.OptionalInt;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
public interface ModifyGuildRolePositions extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @Nullable
    @JsonIgnore
    Snowflake guildId();

    @JsonValue
    List<GuildRolePosition> guildRolePositions();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/roles"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    @ImmutableJson
    interface GuildRolePosition {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        @JsonInclude
        OptionalInt position();

        class Builder extends ImmutableModifyGuildRolePositions.GuildRolePosition.Builder {
            protected Builder() {
            }
        }
    }

    class Builder extends ImmutableModifyGuildRolePositions.Builder {
        protected Builder() {
        }
    }
}
