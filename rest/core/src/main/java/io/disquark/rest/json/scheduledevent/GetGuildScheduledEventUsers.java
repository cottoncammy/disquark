package io.disquark.rest.json.scheduledevent;

import java.util.Optional;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableMulti
abstract class GetGuildScheduledEventUsers extends AbstractRequestMulti<GuildScheduledEvent.User> {

    public abstract Snowflake guildId();

    public abstract Snowflake guildScheduledEventId();

    @Default
    public int limit() {
        return 100;
    }

    @Default
    public boolean withMember() {
        return false;
    }

    public abstract Optional<Snowflake> before();

    public abstract Optional<Snowflake> after();

    @Override
    public void subscribe(Flow.Subscriber<? super GuildScheduledEvent.User> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(GuildScheduledEvent.User[].class))
                .onItem().<GuildScheduledEvent.User> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
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
}
