package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

@ImmutableUni
abstract class ListThreads extends AbstractRequestUni<ListThreadsResult> {

    public abstract String uri();

    public abstract Snowflake channelId();

    public abstract Optional<Instant> before();

    public abstract OptionalInt limit();

    @Override
    public void subscribe(UniSubscriber<? super ListThreadsResult> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(ListThreadsResult.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("channel.id", channelId().getValue());
        if (limit().isPresent()) {
            json.put("limit", limit().getAsInt());
        }

        if (before().isPresent()) {
            json.put("before", ISO_DATE_TIME.format(before().get()));
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, uri()))
                .variables(Variables.variables(json))
                .build();
    }
}
