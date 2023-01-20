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

import org.example.rest.resources.permissions.PermissionFlag;
import org.junit.jupiter.api.Test;

class PermissionFlagsTest {
    private static final long NO_ADMIN = Arrays.stream(PermissionFlag.values())
            .filter(value -> value != PermissionFlag.ADMINISTRATOR)
            .map(PermissionFlag::getValue)
            .mapToLong(i -> (long) i)
            .reduce(0, (left, right) -> left | (1L << right));

    private static final long ALL_PERMS = NO_ADMIN | (1L << PermissionFlag.ADMINISTRATOR.getValue());

    @Test
    void testSerialization() {
        assertEquals("{\"flags\":\"0\"}", Json.encode(new Foo(EnumSet.noneOf(PermissionFlag.class))));
        assertEquals("{\"flags\":\"8\"}", Json.encode(new Foo(EnumSet.of(PermissionFlag.CREATE_INSTANT_INVITE, PermissionFlag.ADMINISTRATOR))));
        assertEquals(String.format("{\"flags\":\"%d\"}", NO_ADMIN), Json.encode(new Foo(EnumSet.complementOf(EnumSet.of(PermissionFlag.ADMINISTRATOR)))));
    }

    @Test
    void testDeserialization() throws JsonProcessingException {
        ObjectMapper objectMapper = DatabindCodec.mapper();
        TypeReference<EnumSet<PermissionFlag>> typeRef = new TypeReference<>() {};
        TypeReference<Optional<EnumSet<PermissionFlag>>> optionalTypeRef = new TypeReference<>() {};

        assertEquals(EnumSet.noneOf(PermissionFlag.class), objectMapper.readValue("0", typeRef));
        assertEquals(EnumSet.allOf(PermissionFlag.class), objectMapper.readValue(Long.toUnsignedString(ALL_PERMS), typeRef));
        assertEquals(Optional.of(EnumSet.allOf(PermissionFlag.class)), objectMapper.readValue(Long.toUnsignedString(ALL_PERMS), optionalTypeRef));
        assertEquals(Optional.empty(), objectMapper.readValue("null", optionalTypeRef));
    }

    private static class Foo {
        private final EnumSet<PermissionFlag> flags;

        @JsonCreator
        public Foo(EnumSet<PermissionFlag> flags) {
            this.flags = flags;
        }

        public EnumSet<PermissionFlag> getFlags() {
            return flags;
        }
    }
}
