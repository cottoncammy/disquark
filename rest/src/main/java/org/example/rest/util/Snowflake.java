package org.example.rest.util;

public class Snowflake {
    private final long value;

    public static Snowflake of(long value) {
        return new Snowflake(value);
    }

    private Snowflake(long value) {
        this.value = value;
    }

    public String asString() {
        return "";
    }
}
