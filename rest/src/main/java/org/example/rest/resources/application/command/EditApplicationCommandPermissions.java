package org.example.rest.resources.application.command;

import static org.example.rest.util.Variables.variables;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

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
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}/permissions"))
                .variables(variables("application.id", applicationId().getValue(), "guild.id", guildId().getValue(), "command.id", commandId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableEditApplicationCommandPermissions.Builder {
        protected Builder() {}
    }
}
