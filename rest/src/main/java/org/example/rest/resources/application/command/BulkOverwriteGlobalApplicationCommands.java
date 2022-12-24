package org.example.rest.resources.application.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;
import java.util.Optional;

@ImmutableJson
public interface BulkOverwriteGlobalApplicationCommands extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonValue
    List<GlobalApplicationCommandOverwrite> globalApplicationCommandOverwrites();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/commands"))
                .variables(Variables.variables().set("application.id", applicationId().getValueAsString()))
                .body(this)
                .build();
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
    interface GlobalApplicationCommandOverwrite extends BulkOverwriteGuildApplicationCommands.GuildApplicationCommandOverwrite {

        @JsonProperty("dm_permission")
        Optional<Boolean> dmPermission();
    }

    class Builder extends ImmutableBulkOverwriteGlobalApplicationCommands.Builder {
        protected Builder() {}
    }
}
