package io.disquark.rest.resources.channel.thread;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.guild.Guild;

@ImmutableJson
@JsonDeserialize(as = ThreadMember.class)
interface ThreadMemberJson {

    Optional<Snowflake> id();

    @JsonProperty("user_id")
    Optional<Snowflake> userId();

    @JsonProperty("join_timestamp")
    Instant joinTimestamp();

    int flags();

    Optional<Guild.Member> member();
}
