package org.example.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.example.rest.jackson.NullableOptionalFilter;
import org.example.rest.jackson.SomeModule;
import org.example.rest.util.NullableOptional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NullableOptionalTest {
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void init() {
        objectMapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new SomeModule())
                .setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.CUSTOM, JsonInclude.Include.NON_ABSENT, NullableOptionalFilter.class, null));
    }

    @Test
    void testNull() throws JsonProcessingException {
        assertEquals("{\"bar\":null}", objectMapper.writeValueAsString(new Foo(NullableOptional.ofNull())));
    }

    @Test
    void testPresent() throws JsonProcessingException {
        assertEquals("{\"bar\":1}", objectMapper.writeValueAsString(new Foo(NullableOptional.of(1))));
    }

    @Test
    void testEmpty() throws JsonProcessingException {
        assertEquals("{}", objectMapper.writeValueAsString(new Foo(NullableOptional.empty())));
    }

    static class Foo {
        private final NullableOptional<Integer> bar;

        @JsonCreator
        public Foo(NullableOptional<Integer> bar) {
            this.bar = bar;
        }

        public NullableOptional<Integer> getBar() {
            return bar;
        }
    }
}
