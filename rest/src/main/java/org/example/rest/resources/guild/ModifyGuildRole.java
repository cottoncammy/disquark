package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.permissions.PermissionFlag;

import java.util.EnumSet;
import java.util.Optional;
import java.util.OptionalInt;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface ModifyGuildRole extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake roleId();

    Optional<String> name();

    Optional<EnumSet<PermissionFlag>> permissions();

    OptionalInt color();

    Optional<Boolean> hoist();

    Optional<String> icon();

    @JsonProperty("unicode_emoji")
    Optional<String> unicodeEmoji();

    Optional<Boolean> mentionable();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/roles/{role.id}"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()).set("role.id", roleId().getValueAsString()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuildRole.Builder {
        protected Builder() {}
    }
}
