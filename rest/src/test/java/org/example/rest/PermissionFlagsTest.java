package org.example.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.DatabindCodec;
import org.example.rest.resources.application.Application;
import org.example.rest.resources.permissions.PermissionFlag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PermissionFlagsTest {
    // TODO stream the values
    private static final long NO_ADMIN = 4398046511095L;
    private static final long ALL_PERMS = NO_ADMIN | (1L << PermissionFlag.ADMINISTRATOR.getValue());
    private static final ObjectMapper objectMapper = DatabindCodec.mapper();

    @Test
    void testSerialization() throws JsonProcessingException {
        assertEquals("0", objectMapper.writeValueAsString(EnumSet.noneOf(PermissionFlag.class)));
        assertEquals("8", objectMapper.writeValueAsString(EnumSet.of(PermissionFlag.CREATE_INSTANT_INVITE, PermissionFlag.ADMINISTRATOR)));
        assertEquals(Long.toUnsignedString(NO_ADMIN), objectMapper.writeValueAsString(EnumSet.allOf(PermissionFlag.class)));
    }

    @Test
    void testDeserialization() throws JsonProcessingException {
        TypeReference<EnumSet<PermissionFlag>> typeRef = new TypeReference<>() {};
        TypeReference<Optional<EnumSet<PermissionFlag>>> optionalTypeRef = new TypeReference<>() {};

        assertEquals(EnumSet.noneOf(PermissionFlag.class), objectMapper.readValue("0", typeRef));
        assertEquals(EnumSet.allOf(PermissionFlag.class), objectMapper.readValue(Long.toUnsignedString(ALL_PERMS), typeRef));
        assertEquals(Optional.of(EnumSet.allOf(PermissionFlag.class)), objectMapper.readValue(Long.toUnsignedString(ALL_PERMS), optionalTypeRef));
        assertEquals(Optional.empty(), objectMapper.readValue("null", optionalTypeRef));
    }
}
