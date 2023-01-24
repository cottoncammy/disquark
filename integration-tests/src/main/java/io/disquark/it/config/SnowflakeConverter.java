package io.disquark.it.config;

import io.disquark.rest.resources.Snowflake;

import org.eclipse.microprofile.config.spi.Converter;

public class SnowflakeConverter implements Converter<Snowflake> {

    @Override
    public Snowflake convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Snowflake.create(Long.parseLong(value));
    }
}
