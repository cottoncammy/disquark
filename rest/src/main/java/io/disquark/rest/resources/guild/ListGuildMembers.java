package io.disquark.rest.resources.guild;

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
public interface ListGuildMembers extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static ListGuildMembers create(Snowflake guildId) {
        return ImmutableListGuildMembers.create(guildId);
    }

    Snowflake guildId();

    @Default
    default int limit() {
        return 1;
    }

    Optional<Snowflake> after();

    @Override
    default Request asRequest() {
        JsonObject json = JsonObject.of("guild.id", guildId().getValue(), "limit", limit());
        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/members{?limit,after}"))
                .variables(Variables.variables(json))
                .build();
    }

    class Builder extends ImmutableListGuildMembers.Builder {
        protected Builder() {
        }
    }
}
