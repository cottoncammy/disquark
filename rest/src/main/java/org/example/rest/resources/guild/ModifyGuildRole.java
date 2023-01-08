package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;
import org.example.immutables.ImmutableJson;
import org.example.rest.jackson.ImageDataSerializer;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.permissions.PermissionFlag;

import java.util.EnumSet;
import java.util.Optional;
import java.util.OptionalInt;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
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

    @JsonSerialize(using = ImageDataSerializer.class)
    Optional<Buffer> icon();

    @JsonProperty("unicode_emoji")
    Optional<String> unicodeEmoji();

    Optional<Boolean> mentionable();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/roles/{role.id}"))
                .variables(variables("guild.id", guildId().getValue(), "role.id", roleId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuildRole.Builder {
        protected Builder() {}
    }
}
