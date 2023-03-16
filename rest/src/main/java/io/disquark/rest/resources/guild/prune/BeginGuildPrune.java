package io.disquark.rest.resources.guild.prune;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class BeginGuildPrune extends AbstractRequestUni<Integer> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract OptionalInt days();

    @JsonProperty("compute_prune_count")
    public abstract Optional<Boolean> computePruneCount();

    @JsonProperty("include_roles")
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
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/prune"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
