package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.vertx.mutiny.core.buffer.Buffer;
import org.apache.tika.Tika;
import org.example.nullableoptional.NullableOptional;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class ImageDataSerializer extends StdSerializer<Buffer> implements ContextualSerializer {
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Tika TIKA = new Tika();

    ImageDataSerializer(JavaType type) {
        super(type);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        JavaType type = property.getType();
        if (type.isTypeOrSubTypeOf(Optional.class) || type.isTypeOrSubTypeOf(NullableOptional.class)) {
            type = type.containedType(0);
        }
        return new ImageDataSerializer(type);
    }

    @Override
    public void serialize(Buffer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        byte[] b = value.getBytes();
        gen.writeString(String.format("data:%s;base64,%s", TIKA.detect(b), BASE64_ENCODER.encodeToString(b)));
    }
}
