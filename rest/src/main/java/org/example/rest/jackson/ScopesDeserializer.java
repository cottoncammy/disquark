package org.example.rest.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.example.rest.resources.oauth2.Scope;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ScopesDeserializer extends JsonDeserializer<List<Scope>> {

    @Override
    public List<Scope> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        Arrays.asList(p.getText().split(" ")).stream().map(s -> );

        return null;
    }
}
