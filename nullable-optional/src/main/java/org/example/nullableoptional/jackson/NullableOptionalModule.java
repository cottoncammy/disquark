package org.example.nullableoptional.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class NullableOptionalModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        context.addSerializers(new NullableOptionalSerializers());
        context.addDeserializers(new NullableOptionalDeserializers());
        context.addTypeModifier(new NullableOptionalTypeModifier());
    }
}
