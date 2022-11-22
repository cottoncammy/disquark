package org.example.rest.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.rest.resources.Snowflake;

public class SomeModule extends SimpleModule {

    public SomeModule() {
        addSerializer(Snowflake.class, new SnowflakeSerializer());
        addDeserializer(Snowflake.class, new SnowflakeDeserializer());
    }
}
