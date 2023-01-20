package org.example.rest.resources.voice;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.guild.Guild;

@ImmutableJson
@JsonDeserialize(as = ImmutableVoiceState.class)
interface VoiceState {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    @JsonProperty("user_id")
    Snowflake userId();

    Optional<Guild.Member> member();

    @JsonProperty("session_id")
    String sessionId();

    boolean deaf();

    boolean mute();

    @JsonProperty("self_deaf")
    boolean selfDeaf();

    @JsonProperty("self_mute")
    boolean selfMute();

    @JsonProperty("self_stream")
    Optional<Boolean> selfStream();

    @JsonProperty("self_video")
    boolean selfVideo();

    boolean suppress();

    @JsonProperty("request_to_speak_timestamp")
    Optional<Instant> requestToSpeakTimestamp();

    class Builder extends ImmutableVoiceState.Builder {
        protected Builder() {
        }
    }
}
