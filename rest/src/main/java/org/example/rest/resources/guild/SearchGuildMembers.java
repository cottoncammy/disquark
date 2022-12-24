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

import static org.example.rest.util.Variables.variables;

@Immutable
@ImmutableStyle
public interface SearchGuildMembers extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static SearchGuildMembers create(Snowflake guildId, String query) {
        return null;
    }

    Snowflake guildId();

    String query();

    @Default
    default int limit() {
        return 1;
    }

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/members/search{?query,limit}"))
                .variables(variables("guild.id", guildId().getValue(), "query", query(), "limit", limit()))
                .build();
    }

    class Builder extends ImmutableSearchGuildMembers.Builder {
        protected Builder() {}
    }
}
