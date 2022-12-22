package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.time.Instant;
import java.util.Optional;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface ModifyCurrentUserVoiceState extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    Optional<Boolean> suppress();

    @JsonProperty("request_to_speak_timestamp")
    Optional<Instant> requestToSpeakTimestamp();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/voice-states/@me"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableModifyCurrentUserVoiceState.Builder {
        protected Builder() {}
    }
}
