package org.example.rest.resources.application.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;

@ImmutableJson
public interface EditApplicationCommandPermissions extends Requestable {

    static EditApplicationCommandPermissions create(Snowflake applicationId, Snowflake guildId, Snowflake commandId, List<GuildApplicationCommandPermissions> permissions) {
        return null;
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake commandId();

    List<GuildApplicationCommandPermissions> permissions();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}/permissions"))
                .variables(Variables.variables().set("application.id", applicationId().getValueAsString()).set("guild.id", guildId().getValueAsString()).set("command.id", commandId().getValueAsString()))
                .body(this)
                .build();
    }
}
