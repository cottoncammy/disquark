package org.example.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.DatabindCodec;
import org.example.rest.resources.application.Application;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationFlagsTest {
    // TODO stream the values
    private static final String ALL_FLAGS = "9433088";
    private static final ObjectMapper objectMapper = DatabindCodec.mapper();

    @Test
    void testSerialization() throws JsonProcessingException {
        assertEquals("0", objectMapper.writeValueAsString(EnumSet.noneOf(Application.Flag.class)));
        assertEquals(ALL_FLAGS, objectMapper.writeValueAsString(EnumSet.allOf(Application.Flag.class)));
    }

    @Test
    void testDeserialization() throws JsonProcessingException {
        ObjectMapper objectMapper = DatabindCodec.mapper();
        TypeReference<Optional<EnumSet<Application.Flag>>> optionalTypeRef = new TypeReference<>() {};
        TypeReference<EnumSet<Application.Flag>> typeRef = new TypeReference<>() {};

        assertEquals(EnumSet.noneOf(Application.Flag.class), objectMapper.readValue("0", typeRef));
        assertEquals(EnumSet.allOf(Application.Flag.class), objectMapper.readValue(ALL_FLAGS, typeRef));
        assertEquals(Optional.of(EnumSet.allOf(Application.Flag.class)), objectMapper.readValue(ALL_FLAGS, optionalTypeRef));
        assertEquals(Optional.empty(), objectMapper.readValue("null", optionalTypeRef));
    }
}
