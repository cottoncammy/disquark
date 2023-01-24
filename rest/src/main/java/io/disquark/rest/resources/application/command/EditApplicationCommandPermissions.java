package io.disquark.rest.resources.application.command;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
public interface EditApplicationCommandPermissions extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake commandId();

    List<ApplicationCommand.Permissions> permissions();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT,
                        "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}/permissions"))
                .variables(variables("application.id", applicationId().getValue(), "guild.id", guildId().getValue(),
                        "command.id", commandId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableEditApplicationCommandPermissions.Builder {
        protected Builder() {
        }
    }
}
