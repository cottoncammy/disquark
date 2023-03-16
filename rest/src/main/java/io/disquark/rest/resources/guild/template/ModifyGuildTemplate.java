package io.disquark.rest.resources.guild.template;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ModifyGuildTemplate extends AbstractRequestUni<GuildTemplate> {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract String templateCode();

    public abstract Optional<String> name();

    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<String> description();

    @Override
    public void subscribe(UniSubscriber<? super GuildTemplate> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(GuildTemplate.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/templates/{template.code}"))
                .variables(variables("guild.id", guildId().getValue(), "template.code", templateCode()))
                .body(this)
                .build();
    }
}
