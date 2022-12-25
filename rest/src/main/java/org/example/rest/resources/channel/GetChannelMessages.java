package org.example.rest.resources.channel;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.immutables.ImmutableBuilder;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Default;

import java.util.Optional;

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
        protected Builder() {}
    }
}
