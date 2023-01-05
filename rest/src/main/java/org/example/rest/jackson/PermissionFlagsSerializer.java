package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.example.rest.resources.permissions.PermissionFlag;

import java.io.IOException;
import java.util.EnumSet;

public class PermissionFlagsSerializer extends JsonSerializer<EnumSet<PermissionFlag>> {

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
