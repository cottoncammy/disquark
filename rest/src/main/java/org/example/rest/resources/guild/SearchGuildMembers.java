package org.example.rest.resources.guild;

import static org.example.rest.util.Variables.variables;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableBuilder;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface SearchGuildMembers extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static SearchGuildMembers create(Snowflake guildId, String query) {
        return ImmutableSearchGuildMembers.create(guildId, query);
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
        protected Builder() {
        }
    }
}
