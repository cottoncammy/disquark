package org.example.rest.resources.guild;

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

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.example.rest.util.Variables.variables;

@Enclosing
@ImmutableJson
public interface ModifyGuildChannelPositions extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonValue
    List<GuildChannelPosition> guildChannelPositions();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/channels"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .build();
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
    interface GuildChannelPosition {

        Snowflake id();

        OptionalInt position();

        @JsonProperty("lock_permissions")
        Optional<Boolean> lockPermissions();

        @JsonProperty("parent_id")
        Optional<Snowflake> parentId();
    }

    class Builder extends ImmutableModifyGuildChannelPositions.Builder {
        protected Builder() {}
    }
}
