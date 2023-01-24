package io.disquark.rest.resources.user;

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
