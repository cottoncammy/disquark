package org.example.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.DatabindCodec;

import org.example.rest.resources.application.Application;
import org.junit.jupiter.api.Test;

class ApplicationFlagsTest {
    private static final String ALL_FLAGS = Long.toUnsignedString(Arrays.stream(Application.Flag.values())
            .map(Application.Flag::getValue)
            .mapToLong(i -> (long) i)
            .reduce(0, (left, right) -> left | (1L << right)));

    @Test
    void testSerialization() {
        assertEquals("{\"flags\":0}", Json.encode(new Foo(EnumSet.noneOf(Application.Flag.class))));
        assertEquals(String.format("{\"flags\":%s}", ALL_FLAGS), Json.encode(new Foo(EnumSet.allOf(Application.Flag.class))));
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

    private static class Foo {
        private final EnumSet<Application.Flag> flags;

        @JsonCreator
        public Foo(EnumSet<Application.Flag> flags) {
            this.flags = flags;
        }

        public EnumSet<Application.Flag> getFlags() {
            return flags;
        }
    }
}
