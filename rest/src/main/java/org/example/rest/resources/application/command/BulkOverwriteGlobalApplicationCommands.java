package org.example.rest.resources.application.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Enclosing;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@Enclosing
@ImmutableJson
public interface BulkOverwriteGlobalApplicationCommands extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @Nullable
    @JsonIgnore
    Snowflake applicationId();

    @JsonValue
    List<GlobalApplicationCommandOverwrite> globalApplicationCommandOverwrites();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/commands"))
                .variables(variables("application.id", applicationId().getValue()))
                .body(this)
                .build();
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
    interface GlobalApplicationCommandOverwrite extends BulkOverwriteGuildApplicationCommands.GuildApplicationCommandOverwrite {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("dm_permission")
        Optional<Boolean> dmPermission();

        class Builder extends ImmutableBulkOverwriteGlobalApplicationCommands.GlobalApplicationCommandOverwrite.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableBulkOverwriteGlobalApplicationCommands.Builder {
        protected Builder() {}
    }
}
