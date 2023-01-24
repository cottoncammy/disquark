package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.permissions.PermissionFlag;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableJson
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
public interface ModifyGuildRole extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake roleId();

    NullableOptional<String> name();

    NullableOptional<EnumSet<PermissionFlag>> permissions();

    NullableOptional<Integer> color();

    @JsonInclude(Include.NON_ABSENT)
    Optional<Boolean> hoist();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    NullableOptional<Buffer> icon();

    @JsonProperty("unicode_emoji")
    NullableOptional<String> unicodeEmoji();

    @JsonInclude(Include.NON_ABSENT)
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
        protected Builder() {
        }
    }
}
