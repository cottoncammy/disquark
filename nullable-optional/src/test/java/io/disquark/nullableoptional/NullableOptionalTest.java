package io.disquark.nullableoptional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.nullableoptional.jackson.NullableOptionalModule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class NullableOptionalTest {
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void init() {
        objectMapper = new ObjectMapper()
                .registerModule(new NullableOptionalModule())
                .setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.CUSTOM,
                        JsonInclude.Include.NON_ABSENT, NullableOptionalFilter.class, null));
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
