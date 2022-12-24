package org.example.rest.resources.channel.thread;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;

import java.time.Instant;
import java.util.Optional;

@ImmutableJson
@JsonDeserialize(as = ImmutableThreadMember.class)
public interface ThreadMember {

    static Builder builder() {
        return new Builder();
    }

    Optional<Snowflake> id();

    @JsonProperty("user_id")
    Optional<Snowflake> userId();

    @JsonProperty("join_timestamp")
    Instant joinTimestamp();

    int flags();

    class Builder extends ImmutableThreadMember.Builder {
        protected Builder() {}
    }
}
