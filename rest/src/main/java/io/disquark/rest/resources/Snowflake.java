package io.disquark.rest.resources;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Snowflake {
    private static final long DISCORD_EPOCH = 1420070400000L;

    private final long value;

    public static Snowflake create(long value) {
        return new Snowflake(value);
    }

    public static Snowflake create(Instant value) {
        return create((requireNonNull(value, "value").toEpochMilli() - DISCORD_EPOCH) << 22);
    }

    @JsonCreator
    public static Snowflake create(String value) {
        return create(Long.parseLong(requireNonNull(value, "value")));
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

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Snowflake that = (Snowflake) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Snowflake{" +
                "value=" + value +
                '}';
    }
}
