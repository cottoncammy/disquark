package io.disquark.rest.resources.channel;

import java.util.Optional;

import io.disquark.immutables.ImmutableBuilder;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface GetChannelMessages extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetChannelMessages create(Snowflake channelId) {
        return ImmutableGetChannelMessages.create(channelId);
    }

    Snowflake channelId();

    Optional<Snowflake> around();

    Optional<Snowflake> before();

    Optional<Snowflake> after();

    @Default
    default int limit() {
        return 50;
    }

    @Override
    default Request asRequest() {
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

    class Builder extends ImmutableGetChannelMessages.Builder {
        protected Builder() {
        }
    }
}
