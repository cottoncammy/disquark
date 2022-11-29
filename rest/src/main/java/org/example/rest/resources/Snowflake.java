package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonValue;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public class Snowflake {
    private static final long DISCORD_EPOCH = 1420070400000L;

    private final long value;

    public static Snowflake create(long value) {
        return new Snowflake(value);
    }

    public static Snowflake create(Instant value) {
        return create((requireNonNull(value).toEpochMilli() - DISCORD_EPOCH) << 22);
    }

    public static Snowflake create(String value) {
        return create(Long.parseLong(requireNonNull(value)));
    }

    private Snowflake(long value) {
        this.value = value;
    }

    @JsonValue
    public long getValue() {
        return value;
    }

    public String getValueAsString() {
        return Long.toUnsignedString(value);
    }

    public Instant getTimestamp() {
        return Instant.ofEpochMilli((value >> 22) + DISCORD_EPOCH);
    }
}
