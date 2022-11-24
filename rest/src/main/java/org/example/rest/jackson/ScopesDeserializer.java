package org.example.rest.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.rest.resources.oauth2.Scope;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScopesDeserializer extends JsonDeserializer<List<Scope>> {

    @Override
    public List<Scope> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String s = p.getText();

        try {
            return Arrays.stream(s.split(" ")).map(Scope::create).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException(p, "Expected a space-separated list of Discord OAuth2 scopes", s, List.class);
        }
    }
}
