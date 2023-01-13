package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Enclosing;

import javax.annotation.Nullable;
import java.util.List;
import java.util.OptionalInt;

import static org.example.rest.util.Variables.variables;

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
            protected Builder() {}
        }
    }

    class Builder extends ImmutableModifyGuildRolePositions.Builder {
        protected Builder() {}
    }
}
