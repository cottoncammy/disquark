package io.disquark.rest.resources.channel.message;

import java.util.Optional;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.emoji.ReactionEmoji;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.user.User;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableMulti
abstract class GetReactions extends AbstractRequestMulti<User> {

    public abstract Snowflake channelId();

    public abstract Snowflake messageId();

    public abstract ReactionEmoji emoji();

    public abstract Optional<Snowflake> after();

    @Default
    public int limit() {
        return 25;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super User> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(User[].class))
                .onItem().<User> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("channel.id", channelId().getValue(), "message.id", messageId().getValue(), "emoji",
                emoji().getValue(), "limit", limit());
        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET,
                        "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}{?after,limit}"))
                .variables(Variables.variables(json))
                .build();
    }
}
