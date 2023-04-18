package io.disquark.rest.json.role;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.vertx.core.http.HttpMethod;

@ImmutableMulti
abstract class ModifyGuildRolePositions extends AbstractRequestMulti<Role> implements Auditable {

    public abstract Snowflake guildId();

    public abstract List<GuildRolePosition> positions();

    @Override
    public void subscribe(Flow.Subscriber<? super Role> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(Role[].class))
                .onItem().<Role> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/roles"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(positions())
                .auditLogReason(auditLogReason())
                .build();
    }
}
