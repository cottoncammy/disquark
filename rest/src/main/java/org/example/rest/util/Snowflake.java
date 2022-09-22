package org.example.rest.util;

import java.time.Instant;

public class Snowflake {
    private static final long DISCORD_EPOCH = 1420070400000L;
    private final long value;

    public static Snowflake of(long value) {
        return new Snowflake(value);
    }

    public static Snowflake of(Instant value) {
        return null;
    }

    private Snowflake(long value) {
        this.value = value;
    }

    public Instant getTimestamp() {
        return null;
    }

    public String asString() {
        return "";
    }
}
