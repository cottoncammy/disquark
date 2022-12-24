package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface ModifyUserVoiceState extends Requestable {

    static ModifyUserVoiceState create(Snowflake guildId, Snowflake userId, Snowflake channelId, boolean suppress) {
        return null;
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake userId();

    @JsonProperty("channel_id")
    Snowflake channelId();

    boolean suppress();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/voice-states/{user.id}"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()).set("user.id", userId().getValueAsString()))
                .body(this)
                .build();
    }
}
