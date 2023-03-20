package io.disquark.rest.json.member;

import static io.disquark.rest.util.Variables.variables;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class ModifyGuildMember extends AbstractRequestUni<GuildMember> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake userId();

    public abstract NullableOptional<String> nick();

    public abstract NullableOptional<List<Snowflake>> roles();

    @JsonInclude(Include.NON_ABSENT)
    public abstract Optional<Boolean> mute();

    @JsonInclude(Include.NON_ABSENT)
    public abstract Optional<Boolean> deaf();

    @JsonProperty("channel_id")
    public abstract NullableOptional<Snowflake> channelId();

    @JsonProperty("communication_disabled_until")
    public abstract NullableOptional<Instant> communicationDisabledUntil();

    @Override
    public void subscribe(UniSubscriber<? super GuildMember> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(GuildMember.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/members/{user.id}"))
                .variables(variables("guild.id", guildId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
