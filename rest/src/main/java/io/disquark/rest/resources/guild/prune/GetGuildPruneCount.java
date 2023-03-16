package io.disquark.rest.resources.guild.prune;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableUni
abstract class GetGuildPruneCount extends AbstractRequestUni<Integer> {

    public abstract Snowflake guildId();

    @Default
    public int limit() {
        return 7;
    }

    public abstract Optional<List<Snowflake>> includeRoles();

    @Override
    public void subscribe(UniSubscriber<? super Integer> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(GuildPruneResponse.class))
                .flatMap(result -> Uni.createFrom().optional(result.getPruned()))
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("guild.id", guildId().getValue(), "limit", limit());
        if (includeRoles().isPresent()) {
            json.put("include_roles",
                    includeRoles().get().stream().map(Snowflake::getValueAsString).collect(Collectors.joining(",")));
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/prune{?limit,include_roles}"))
                .variables(Variables.variables(json))
                .build();
    }
}
