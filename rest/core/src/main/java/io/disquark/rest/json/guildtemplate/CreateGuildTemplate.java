package io.disquark.rest.json.guildtemplate;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class CreateGuildTemplate extends AbstractRequestUni<GuildTemplate> {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract String name();

    public abstract Optional<String> description();

    @Override
    public void subscribe(UniSubscriber<? super GuildTemplate> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(GuildTemplate.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/templates"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .build();
    }
}
