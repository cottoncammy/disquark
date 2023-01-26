package io.disquark.rest.resources.guild.prune;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
public interface GetGuildPruneCount extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetGuildPruneCount create(Snowflake guildId) {
        return ImmutableGetGuildPruneCount.create(guildId);
    }

    Snowflake guildId();

    @Default
    default int limit() {
        return 7;
    }

    Optional<List<Snowflake>> includeRoles();

    @Override
    default Request asRequest() {
        JsonObject json = JsonObject.of("guild.id", guildId().getValue(), "limit", limit());
        if (includeRoles().isPresent()) {
            json.put("include_roles",
                    includeRoles().get().stream().map(Snowflake::getValueAsString).collect(Collectors.joining(",")));
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/prune{?limit,include_roles}"))
                .variables(Variables.variables(json))
                .build();
    }

    class Builder extends ImmutableGetGuildPruneCount.Builder {
        protected Builder() {
        }
    }
}