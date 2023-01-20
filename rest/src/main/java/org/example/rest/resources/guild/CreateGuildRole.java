package org.example.rest.resources.guild;

import static org.example.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import org.immutables.value.Value.Redacted;

@ImmutableJson
public interface CreateGuildRole extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    Optional<String> name();

    Optional<EnumSet<PermissionFlag>> permissions();

    OptionalInt color();

    Optional<Boolean> hoist();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    Optional<Buffer> icon();

    @JsonProperty("unicode_emoji")
    Optional<String> unicodeEmoji();

    Optional<Boolean> mentionable();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/roles"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableCreateGuildRole.Builder {
        protected Builder() {
        }
    }
}
