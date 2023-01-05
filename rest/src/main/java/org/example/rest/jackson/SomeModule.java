package org.example.rest.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import org.example.rest.resources.FlagEnum;
import org.example.rest.resources.permissions.PermissionFlag;

import java.time.Instant;
import java.util.EnumSet;

public class SomeModule extends SimpleModule {

    public SomeModule() {
        addDeserializer(EnumSet.class, new FlagsDeserializer<>());
        addDeserializer(Instant.class, new InstantDeserializer());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(new FlagsSerializer(context.getTypeFactory().constructCollectionType(EnumSet.class, FlagEnum.class)));
        context.addSerializers(serializers);
    }
}
