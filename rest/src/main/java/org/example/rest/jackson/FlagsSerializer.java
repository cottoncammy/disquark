package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.example.rest.resources.FlagEnum;
import org.example.rest.resources.permissions.PermissionFlag;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Optional;

public class FlagsSerializer extends StdSerializer<EnumSet<? extends FlagEnum>> implements ContextualSerializer {

    FlagsSerializer(JavaType type) {
        super(type);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            throw new IllegalStateException("Cannot construct contextual serializer for root value");
        }

        JavaType type = property.getType();
        if (type.isTypeOrSubTypeOf(Optional.class)) {
            type = type.containedType(0);
        }

        if (type.containedType(0).getRawClass() == PermissionFlag.class) {
            return new PermissionFlagsSerializer(type);
        }

        return new FlagsSerializer(type);
    }

    @Override
    public void serialize(EnumSet<? extends FlagEnum> values, JsonGenerator gen, SerializerProvider provider) throws IOException {
        long flags = 0;
        for (FlagEnum value : values) {
            flags |= (1L << value.getValue());
        }
        gen.writeNumber(flags);
    }
}
