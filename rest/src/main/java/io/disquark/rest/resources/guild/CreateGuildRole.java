package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.permissions.PermissionFlag;
import io.disquark.rest.resources.permissions.Role;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class CreateGuildRole extends AbstractRequestUni<Role> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract Optional<String> name();

    public abstract Optional<EnumSet<PermissionFlag>> permissions();

    public abstract OptionalInt color();

    public abstract Optional<Boolean> hoist();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    public abstract Optional<Buffer> icon();

    @JsonProperty("unicode_emoji")
    public abstract Optional<String> unicodeEmoji();

    public abstract Optional<Boolean> mentionable();

    @Override
    public void subscribe(UniSubscriber<? super Role> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Role.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/roles"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
