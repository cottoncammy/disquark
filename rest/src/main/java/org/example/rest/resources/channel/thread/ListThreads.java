package org.example.rest.resources.channel.thread;

import org.example.rest.immutables.ImmutableStyle;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Immutable;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

@Immutable
@ImmutableStyle
public interface ListThreads {

    static Builder builder() {
        return new Builder();
    }

    static ListThreads create(Snowflake channelId) {
        return null;
    }

    Snowflake channelId();

    Optional<Instant> before();

    OptionalInt limit();

    class Builder extends ImmutableListThreads.Builder {
        protected Builder() {}
    }
}
