package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.example.rest.resources.Snowflake;

import java.io.IOException;

public class SnowflakeSerializer extends JsonSerializer<Snowflake> {

    @Override
    public void serialize(Snowflake value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeNumber(value.getValue());
    }
}
