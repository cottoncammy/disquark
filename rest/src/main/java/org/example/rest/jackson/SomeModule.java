package org.example.rest.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.Instant;
import java.util.EnumSet;

public class SomeModule extends SimpleModule {

    public SomeModule() {
        addDeserializer(EnumSet.class, new FlagsDeserializer<>());
        addDeserializer(Instant.class, new InstantDeserializer());
    }
}
