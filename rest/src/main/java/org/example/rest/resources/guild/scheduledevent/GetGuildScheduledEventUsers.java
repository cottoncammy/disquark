package org.example.rest.resources.guild.scheduledevent;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableStyle;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Default;

import java.util.Optional;

@Immutable
@ImmutableStyle
public interface GetGuildScheduledEventUsers extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    Snowflake guildId();

    Snowflake guildScheduledEventId();

    @Default
    default int limit() {
        return 100;
    }

    @Default
    default boolean withMember() {
        return false;
    }

    Optional<Snowflake> before();

    Optional<Snowflake> after();

    @Override
    default Request asRequest() {
        JsonObject json = JsonObject.of("guild.id", guildId().getValue(), "guild_scheduled_event.id", guildScheduledEventId().getValue(), "limit", limit(), "with_member", withMember());
        if (before().isPresent()) {
            json.put("before", before().get().getValue());
        }

        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}/users{?limit,with_member,before,after}"))
                .variables(Variables.variables(json))
                .build();
    }

    class Builder extends ImmutableGetGuildScheduledEventUsers.Builder {
        protected Builder() {}
    }
}
