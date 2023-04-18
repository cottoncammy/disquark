package io.disquark.rest.json.role;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class ModifyGuildRole extends AbstractRequestUni<Role> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake roleId();

    public abstract NullableOptional<String> name();

    public abstract NullableOptional<EnumSet<PermissionFlag>> permissions();

    public abstract NullableOptional<Integer> color();

    @JsonInclude(Include.NON_ABSENT)
    public abstract Optional<Boolean> hoist();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    public abstract NullableOptional<Buffer> icon();

    @JsonProperty("unicode_emoji")
    public abstract NullableOptional<String> unicodeEmoji();

    @JsonInclude(Include.NON_ABSENT)
    public abstract Optional<Boolean> mentionable();

    @Override
    public void subscribe(UniSubscriber<? super Role> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Role.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/roles/{role.id}"))
                .variables(variables("guild.id", guildId().getValue(), "role.id", roleId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
