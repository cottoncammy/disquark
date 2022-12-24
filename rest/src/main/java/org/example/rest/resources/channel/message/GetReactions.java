package org.example.rest.resources.channel.message;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.emoji.ReactionEmoji;
import org.example.rest.immutables.ImmutableStyle;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Default;

import java.util.Optional;

@Immutable
@ImmutableStyle
public interface GetReactions extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetReactions create(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return null;
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
        JsonObject json = JsonObject.of("channel.id", channelId().getValue(), "message.id", messageId().getValue(), "emoji", emoji().getValue(), "limit", limit());
        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}{?after,limit}"))
                .variables(Variables.variables(json))
                .build();
    }

    class Builder extends ImmutableGetReactions.Builder {
        protected Builder() {}
    }
}
