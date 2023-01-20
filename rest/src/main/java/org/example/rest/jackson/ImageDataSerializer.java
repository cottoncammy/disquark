package org.example.rest.jackson;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.vertx.mutiny.core.buffer.Buffer;

import org.example.rest.util.Tika;

public class ImageDataSerializer extends JsonSerializer<Buffer> {
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    @Override
    public void serialize(Buffer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        byte[] b = value.getBytes();
        gen.writeString(String.format("data:%s;base64,%s", Tika.detect(b), BASE64_ENCODER.encodeToString(b)));
    }
}
