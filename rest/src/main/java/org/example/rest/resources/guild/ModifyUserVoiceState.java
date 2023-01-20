package org.example.rest.resources.guild;

import static org.example.rest.util.Variables.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface ModifyUserVoiceState extends Requestable {

    static ModifyUserVoiceState create(Snowflake guildId, Snowflake userId, Snowflake channelId, boolean suppress) {
        return ImmutableModifyUserVoiceState.create(guildId, userId, channelId, suppress);
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
                .variables(variables("guild.id", guildId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .build();
    }
}
