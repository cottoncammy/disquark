package io.disquark.rest.jackson;

import java.time.Instant;
import java.util.EnumSet;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import io.disquark.rest.json.FlagEnum;

public class DisQuarkModule extends SimpleModule {

    public DisQuarkModule() {
        addDeserializer(EnumSet.class, new FlagsDeserializer<>());
        addDeserializer(Instant.class, new InstantDeserializer());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(
                new FlagsSerializer(context.getTypeFactory().constructCollectionType(EnumSet.class, FlagEnum.class)));
        context.addSerializers(serializers);
    }
}
