package org.example.nullableoptional.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.example.nullableoptional.NullableOptional;

public class NullableOptionalDeserializers extends Deserializers.Base {

    @Override
    public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) throws JsonMappingException {
        if (refType.isTypeOrSubTypeOf(NullableOptional.class)) {
            return new NullableOptionalDeserializer(refType, null, contentTypeDeserializer, contentDeserializer);
        }
        return null;
    }
}
