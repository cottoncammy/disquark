package io.disquark.rest.jackson;

import java.io.IOException;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import io.disquark.rest.resources.permissions.PermissionFlag;

public class PermissionFlagsSerializer extends StdSerializer<EnumSet<PermissionFlag>> {

    PermissionFlagsSerializer(JavaType type) {
        super(type);
    }

    @Override
    public void serialize(EnumSet<PermissionFlag> values, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (values.contains(PermissionFlag.ADMINISTRATOR)) {
            gen.writeString(Long.toUnsignedString(1L << PermissionFlag.ADMINISTRATOR.getValue()));
            return;
        }

        long flags = 0;
        for (PermissionFlag value : values) {
            flags |= (1L << value.getValue());
        }
        gen.writeString(Long.toUnsignedString(flags));
    }
}
