package org.example.rest;

import io.vertx.core.json.Json;
import org.example.rest.resources.permissions.PermissionFlag;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PermissionFlagsTest {

    @Test
    void testSerialization() {
        assertEquals("0", Json.encode(EnumSet.noneOf(PermissionFlag.class)));
        assertEquals("8", Json.encode(EnumSet.of(PermissionFlag.ADMINISTRATOR)));
        assertEquals("4398046511095", Json.encode(EnumSet.allOf(PermissionFlag.class)));
    }

    @Test
    void testDeserialization() {
        assertEquals(EnumSet.noneOf(PermissionFlag.class), Json.decodeValue("0", EnumSet.class));
        assertEquals(EnumSet.of(PermissionFlag.ADMINISTRATOR), Json.decodeValue("8", EnumSet.class));
        assertEquals(EnumSet.allOf(PermissionFlag.class), Json.decodeValue("4398046511095", EnumSet.class));
    }
}
