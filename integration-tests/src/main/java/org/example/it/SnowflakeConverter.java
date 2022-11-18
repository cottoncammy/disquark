package org.example.it;

import org.eclipse.microprofile.config.spi.Converter;
import org.example.rest.resources.Snowflake;

public class SnowflakeConverter implements Converter<Snowflake> {

    @Override
    public Snowflake convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Snowflake.create(Long.parseLong(value));
    }
}
