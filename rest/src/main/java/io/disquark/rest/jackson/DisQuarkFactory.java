package io.disquark.rest.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.disquark.nullableoptional.jackson.NullableOptionalModule;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.core.json.jackson.JacksonFactory;
import io.vertx.core.spi.JsonFactory;
import io.vertx.core.spi.json.JsonCodec;

public class DisQuarkFactory implements JsonFactory {

    private static void customizeObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new Jdk8Module())
                .registerModule(new NullableOptionalModule())
                .registerModule(new DisQuarkModule())
                .addHandler(new UnknownPropertyHandler())
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
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
