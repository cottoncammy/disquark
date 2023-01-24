package io.disquark.rest.resources.channel.message;

import java.util.Optional;

import io.disquark.immutables.ImmutableBuilder;
import io.disquark.rest.emoji.ReactionEmoji;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface GetReactions extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetReactions create(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return ImmutableGetReactions.create(channelId, messageId, emoji);
    }

    Snowflake channelId();

    Snowflake messageId();

    ReactionEmoji emoji();

    Optional<Snowflake> after();

    @Default
    default int limit() {
        return 25;
    }

    @Override
    default Request asRequest() {
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

    class Builder extends ImmutableGetReactions.Builder {
        protected Builder() {
        }
    }
}
