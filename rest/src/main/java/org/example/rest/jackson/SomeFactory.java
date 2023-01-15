package org.example.rest.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.core.json.jackson.JacksonFactory;
import io.vertx.core.spi.JsonFactory;
import io.vertx.core.spi.json.JsonCodec;
import org.example.nullableoptional.jackson.NullableOptionalModule;

public class SomeFactory implements JsonFactory {

    private static void customizeObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new Jdk8Module())
                .registerModule(new SomeModule())
                .registerModule(new NullableOptionalModule())
                .addHandler(new UnknownPropertyHandler())
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    static {
        customizeObjectMapper(DatabindCodec.mapper());
        customizeObjectMapper(DatabindCodec.prettyMapper());
    }

    @Override
    public JsonCodec codec() {
        return JacksonFactory.CODEC;
    }
}
