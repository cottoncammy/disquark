package io.disquark.rest.resources.channel;

import java.util.Optional;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableMulti
abstract class GetChannelMessages extends AbstractRequestMulti<Message> {

    public abstract Snowflake channelId();

    public abstract Optional<Snowflake> around();

    public abstract Optional<Snowflake> before();

    public abstract Optional<Snowflake> after();

    @Default
    public int limit() {
        return 50;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Message> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(Message[].class))
                .onItem().<Message> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("channel.id", channelId().getValue(), "limit", limit());
        if (around().isPresent()) {
            json.put("around", around().get().getValue());
        }

        if (before().isPresent()) {
            json.put("before", before().get().getValue());
        }

        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/channels/{channel.id}/messages{?around,before,after,limit}"))
                .variables(Variables.variables(json))
                .build();
    }
}
