package io.disquark.rest.json.auditlog;

import java.util.Optional;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableUni
abstract class GetGuildAuditLog extends AbstractRequestUni<AuditLog> {

    public abstract Snowflake guildId();

    public abstract Optional<Snowflake> userId();

    public abstract Optional<AuditLog.Event> actionType();

    public abstract Optional<Snowflake> before();

    @Default
    public int limit() {
        return 50;
    }

    @Override
    public void subscribe(UniSubscriber<? super AuditLog> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(AuditLog.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("guild.id", guildId().getValue(), "limit", limit());
        if (userId().isPresent()) {
            json.put("user_id", userId().get().getValue());
        }

        if (actionType().isPresent()) {
            json.put("action_type", actionType().get().getValue());
        }

        if (before().isPresent()) {
            json.put("before", before().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/audit-logs{?user_id,action_type,before,limit}"))
                .variables(Variables.variables(json))
                .build();
    }
}
