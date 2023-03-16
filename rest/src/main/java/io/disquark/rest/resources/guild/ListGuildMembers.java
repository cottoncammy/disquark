package io.disquark.rest.resources.guild;

import java.util.Optional;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableMulti
abstract class ListGuildMembers extends AbstractRequestMulti<Guild.Member> {

    public abstract Snowflake guildId();

    @Default
    public int limit() {
        return 1;
    }

    public abstract Optional<Snowflake> after();

    @Override
    public void subscribe(Flow.Subscriber<? super Guild.Member> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(Guild.Member[].class))
                .onItem().<Guild.Member> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("guild.id", guildId().getValue(), "limit", limit());
        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/members{?limit,after}"))
                .variables(Variables.variables(json))
                .build();
    }
}
