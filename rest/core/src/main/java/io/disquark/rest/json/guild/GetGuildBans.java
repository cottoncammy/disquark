package io.disquark.rest.json.guild;

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
abstract class GetGuildBans extends AbstractRequestMulti<Guild.Ban> {

    public abstract Snowflake guildId();

    @Default
    public int limit() {
        return 1000;
    }

    public abstract Optional<Snowflake> before();

    public abstract Optional<Snowflake> after();

    @Override
    public void subscribe(Flow.Subscriber<? super Guild.Ban> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(Guild.Ban[].class))
                .onItem().<Guild.Ban> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("guild.id", guildId().getValue(), "limit", limit());
        if (before().isPresent()) {
            json.put("before", before().get().getValue());
        }

        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/bans"))
                .variables(Variables.variables(json))
                .build();
    }
}
