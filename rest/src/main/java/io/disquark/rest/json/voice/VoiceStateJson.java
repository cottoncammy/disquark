package io.disquark.rest.json.voice;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.member.GuildMember;

@ImmutableJson
@JsonDeserialize(as = VoiceState.class)
interface VoiceStateJson {

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    @JsonProperty("user_id")
    Snowflake userId();

    Optional<GuildMember> member();

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
}
