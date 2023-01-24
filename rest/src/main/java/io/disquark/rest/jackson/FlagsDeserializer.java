package io.disquark.rest.jackson;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import io.disquark.rest.resources.FlagEnum;

public class FlagsDeserializer<E extends Enum<E> & FlagEnum> extends JsonDeserializer<EnumSet<E>>
        implements ContextualDeserializer {
    private final JavaType enumType;

    private FlagsDeserializer(JavaType enumType) {
        this.enumType = enumType;
    }

    public FlagsDeserializer() {
        this(null);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) {
        JavaType type = property == null ? ctx.getContextualType() : property.getType();
        if (type.isTypeOrSubTypeOf(Optional.class)) {
            type = type.containedType(0);
        }
        return new FlagsDeserializer<>(type.containedType(0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public EnumSet<E> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        long flags = p.getValueAsLong();
        EnumSet<E> set = EnumSet.noneOf((Class<E>) enumType.getRawClass());
        for (E value : (E[]) enumType.getRawClass().getEnumConstants()) {
            if ((flags & (1L << value.getValue())) != 0) {
                set.add(value);
            }
        }
        return set;
    }
}
