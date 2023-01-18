package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import io.vertx.mutiny.core.buffer.Buffer;
import org.example.rest.util.Tika;

import java.io.IOException;
import java.util.Base64;

public class ImageDataSerializer extends JsonSerializer<Buffer> {
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    @Override
    public void serialize(Buffer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        byte[] b = value.getBytes();
        gen.writeString(String.format("data:%s;base64,%s", Tika.detect(b), BASE64_ENCODER.encodeToString(b)));
    }
}
