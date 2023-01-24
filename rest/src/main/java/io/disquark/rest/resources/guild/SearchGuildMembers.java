package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import io.disquark.immutables.ImmutableBuilder;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

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
