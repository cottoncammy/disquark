package io.disquark.nullableoptional.jackson;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ReferenceTypeSerializer;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.util.NameTransformer;

import io.disquark.nullableoptional.NullableOptional;

public class NullableOptionalSerializer extends ReferenceTypeSerializer<NullableOptional<?>> {

    protected NullableOptionalSerializer(ReferenceTypeSerializer<?> base, BeanProperty property, TypeSerializer vts,
            JsonSerializer<?> valueSer, NameTransformer unwrapper, Object suppressableValue, boolean suppressNulls) {
        super(base, property, vts, valueSer, unwrapper, suppressableValue, suppressNulls);
    }

    public NullableOptionalSerializer(ReferenceType fullType, boolean staticTyping, TypeSerializer vts,
            JsonSerializer<Object> ser) {
        super(fullType, staticTyping, vts, ser);
    }

    @Override
    protected ReferenceTypeSerializer<NullableOptional<?>> withResolved(BeanProperty prop, TypeSerializer vts,
            JsonSerializer<?> valueSer, NameTransformer unwrapper) {
        return new NullableOptionalSerializer(this, prop, vts, valueSer, unwrapper, _suppressableValue, _suppressNulls);
    }

    @Override
    public ReferenceTypeSerializer<NullableOptional<?>> withContentInclusion(Object suppressableValue, boolean suppressNulls) {
        return new NullableOptionalSerializer(this, _property, _valueTypeSerializer, _valueSerializer, _unwrapper,
                suppressableValue, suppressNulls);
    }

    @Override
    protected boolean _isValuePresent(NullableOptional<?> value) {
        return value.toOptional().isPresent();
    }

    @Override
    protected Object _getReferenced(NullableOptional<?> value) {
        return value.toOptional().get();
    }

    @Override
    protected Object _getReferencedIfPresent(NullableOptional<?> value) {
        return value.toOptional().isEmpty() ? null : value.toOptional().get();
    }
}
