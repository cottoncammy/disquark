package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public class InstantDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        String s = p.getText();
        try {
            return Instant.from(ISO_DATE_TIME.parse(s));
        } catch (DateTimeException e) {
            throw new InvalidFormatException(p, "Expected an ISO 8601 formatted date time", s, Instant.class);
        }
    }
}
