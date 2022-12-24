package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.example.rest.resources.FlagEnum;

import java.io.IOException;
import java.util.EnumSet;

public class FlagsSerializer extends StdSerializer<EnumSet<? extends FlagEnum>> {

    FlagsSerializer(JavaType type) {
        super(type);
    }

    @Override
    public void serialize(EnumSet<? extends FlagEnum> values, JsonGenerator gen, SerializerProvider provider) throws IOException {
        long flags = 0;
        for (FlagEnum value : values) {
            flags |= value.getValue();
        }
        gen.writeNumber(flags);
    }
}
