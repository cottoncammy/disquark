package org.example.nullableoptional.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.example.nullableoptional.NullableOptional;

public class NullableOptionalSerializers extends Serializers.Base {

    @Override
    public JsonSerializer<?> findReferenceSerializer(SerializationConfig config, ReferenceType type, BeanDescription beanDesc, TypeSerializer contentTypeSerializer, JsonSerializer<Object> contentValueSerializer) {
        if (type.isTypeOrSubTypeOf(NullableOptional.class)) {
            boolean staticTyping = contentTypeSerializer == null && config.isEnabled(MapperFeature.USE_STATIC_TYPING);
            return new NullableOptionalSerializer(type, staticTyping, contentTypeSerializer, contentValueSerializer);
        }
        return null;
    }
}
