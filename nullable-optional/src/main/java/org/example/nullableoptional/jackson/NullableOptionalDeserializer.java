package org.example.nullableoptional.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.example.nullableoptional.NullableOptional;

public class NullableOptionalDeserializer extends ReferenceTypeDeserializer<NullableOptional<?>> {

    public NullableOptionalDeserializer(JavaType fullType, ValueInstantiator vi, TypeDeserializer typeDeser,
            JsonDeserializer<?> deser) {
        super(fullType, vi, typeDeser, deser);
    }

    @Override
    protected ReferenceTypeDeserializer<NullableOptional<?>> withResolved(TypeDeserializer typeDeser,
            JsonDeserializer<?> valueDeser) {
        return new NullableOptionalDeserializer(_fullType, _valueInstantiator, typeDeser, valueDeser);
    }

    @Override
    public NullableOptional<?> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return NullableOptional.ofNull();
    }

    @Override
    public NullableOptional<?> referenceValue(Object contents) {
        return NullableOptional.of(contents);
    }

    @Override
    public NullableOptional<?> updateReference(NullableOptional<?> reference, Object contents) {
        return NullableOptional.of(contents);
    }

    @Override
    public Object getReferenced(NullableOptional<?> reference) {
        return reference.toOptional().orElse(null);
    }
}
