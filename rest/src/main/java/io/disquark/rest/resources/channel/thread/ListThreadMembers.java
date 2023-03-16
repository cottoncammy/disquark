package io.disquark.rest.resources.channel.thread;

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
abstract class ListThreadMembers extends AbstractRequestMulti<ThreadMember> {

    public abstract Snowflake channelId();

    @Default
    public boolean withMember() {
        return false;
    }

    public abstract Optional<Snowflake> after();

    @Default
    public int limit() {
        return 100;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ThreadMember> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(ThreadMember[].class))
                .onItem().<ThreadMember> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("channel.id", channelId().getValue(), "with_member", withMember(), "limit", limit());
        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/channels/{channel.id}/thread-members{?with_member,after,limit}"))
                .variables(Variables.variables(json))
                .build();
    }
}
