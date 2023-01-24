package io.disquark.rest.resources.channel;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

import io.disquark.immutables.ImmutableBuilder;
import io.disquark.rest.resources.Snowflake;

@ImmutableBuilder
public interface ListThreads {

    static Builder builder() {
        return new Builder();
    }

    static ListThreads create(Snowflake channelId) {
        return ImmutableListThreads.create(channelId);
    }

    Snowflake channelId();

    Optional<Instant> before();

    OptionalInt limit();

    class Builder extends ImmutableListThreads.Builder {
        protected Builder() {
        }
    }
}
