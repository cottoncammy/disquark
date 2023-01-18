package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UnknownPropertyHandler extends DeserializationProblemHandler {
    private static final Logger LOG = LoggerFactory.getLogger(UnknownPropertyHandler.class);

    @Override
    public boolean handleUnknownProperty(DeserializationContext ctx, JsonParser p, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
        LOG.trace("Skipping unknown property \"{}\" while deserializing {}",
                propertyName, beanOrClass.getClass().getCanonicalName());

        p.skipChildren();
        return true;
    }
}
