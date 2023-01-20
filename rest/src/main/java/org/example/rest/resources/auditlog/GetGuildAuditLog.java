package org.example.rest.resources.auditlog;

import java.util.Optional;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.example.immutables.ImmutableBuilder;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface GetGuildAuditLog extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetGuildAuditLog create(Snowflake guildId) {
        return ImmutableGetGuildAuditLog.create(guildId);
    }

    Snowflake guildId();

    Optional<Snowflake> userId();

    Optional<AuditLog.Event> actionType();

    Optional<Snowflake> before();

    @Default
    default int limit() {
        return 50;
    }

    @Override
    default Request asRequest() {
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

    class Builder extends ImmutableGetGuildAuditLog.Builder {
        protected Builder() {
        }
    }
}
