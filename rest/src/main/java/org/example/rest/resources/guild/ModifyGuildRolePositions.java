package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Enclosing;

import java.util.List;
import java.util.OptionalInt;

@Enclosing
@ImmutableJson
public interface ModifyGuildRolePositions extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonValue
    List<GuildRolePosition> guildRolePositions();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/roles"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
    interface GuildRolePosition {

        Snowflake id();

        OptionalInt position();
    }

    class Builder extends ImmutableModifyGuildRolePositions.Builder {
        protected Builder() {}
    }
}
