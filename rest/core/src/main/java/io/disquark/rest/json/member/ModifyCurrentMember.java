package io.disquark.rest.json.member;

import static io.disquark.rest.util.Variables.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
abstract class ModifyCurrentMember extends AbstractRequestUni<GuildMember> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<String> nick();

    @Override
    public void subscribe(UniSubscriber<? super GuildMember> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(GuildMember.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/members/@me"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
