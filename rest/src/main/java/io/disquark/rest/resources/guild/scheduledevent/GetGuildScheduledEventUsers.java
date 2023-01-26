package io.disquark.rest.resources.guild.scheduledevent;

import java.util.Optional;

import io.disquark.immutables.ImmutableBuilder;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface GetGuildScheduledEventUsers extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetGuildScheduledEventUsers create(Snowflake guildId, Snowflake guildScheduledEventId) {
        return ImmutableGetGuildScheduledEventUsers.create(guildId, guildScheduledEventId);
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
        JsonObject json = JsonObject.of("guild.id", guildId().getValue(), "guild_scheduled_event.id",
                guildScheduledEventId().getValue(), "limit", limit(), "with_member", withMember());
        if (before().isPresent()) {
            json.put("before", before().get().getValue());
        }

        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET,
                        "/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}/users{?limit,with_member,before,after}"))
                .variables(Variables.variables(json))
                .build();
    }

    class Builder extends ImmutableGetGuildScheduledEventUsers.Builder {
        protected Builder() {
        }
    }
}