package org.example.rest.resources.guild;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;
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
public interface ListGuildMembers extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static ListGuildMembers create(Snowflake guildId) {
        return null;
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
        protected Builder() {}
    }
}
