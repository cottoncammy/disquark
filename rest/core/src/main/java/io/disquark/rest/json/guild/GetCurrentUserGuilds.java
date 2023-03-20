package io.disquark.rest.json.guild;

import java.util.Optional;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.partial.PartialGuild;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableMulti
abstract class GetCurrentUserGuilds extends AbstractRequestMulti<PartialGuild> {

    public abstract Optional<Snowflake> before();

    public abstract Optional<Snowflake> after();

    @Default
    public int limit() {
        return 200;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super PartialGuild> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(PartialGuild[].class))
                .onItem().<PartialGuild> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("limit", limit());
        if (before().isPresent()) {
            json.put("before", before().get().getValue());
        }

        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/users/@me/guilds{?before,after,limit}"))
                .variables(Variables.variables(json))
                .build();
    }
}
