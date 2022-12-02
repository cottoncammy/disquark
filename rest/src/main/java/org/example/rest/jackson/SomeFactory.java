package org.example.rest.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.core.json.jackson.JacksonFactory;
import io.vertx.core.spi.JsonFactory;
import io.vertx.core.spi.json.JsonCodec;

public class SomeFactory implements JsonFactory {

    static {
        Jdk8Module jdk8Module = new Jdk8Module();
        DatabindCodec.mapper().registerModule(jdk8Module);
        DatabindCodec.prettyMapper().registerModule(jdk8Module);

        SomeModule someModule = new SomeModule();
        DatabindCodec.mapper().registerModule(someModule);
        DatabindCodec.prettyMapper().registerModule(someModule);

        DatabindCodec.mapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DatabindCodec.prettyMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public JsonCodec codec() {
        return JacksonFactory.CODEC;
    }
}
