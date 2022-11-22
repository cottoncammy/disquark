package org.example.rest.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.rest.resources.Snowflake;

import java.io.IOException;

public class SnowflakeDeserializer extends JsonDeserializer<Snowflake> {

    @Override
    public Snowflake deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonParseException {
        String s = p.getText();

        try {
            return Snowflake.create(s);
        } catch (NullPointerException | NumberFormatException e) {
            throw new InvalidFormatException(p, "Expected a Snowflake format string", s, Snowflake.class);
        }
    }
}
