package org.example.rest.resources.user;

import java.util.Optional;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.example.immutables.ImmutableBuilder;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface GetCurrentUserGuilds extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetCurrentUserGuilds create() {
        return builder().build();
    }

    Optional<Snowflake> before();

    Optional<Snowflake> after();

    @Default
    default int limit() {
        return 200;
    }

    @Override
    default Request asRequest() {
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

    class Builder extends ImmutableGetCurrentUserGuilds.Builder {
        protected Builder() {
        }
    }
}
