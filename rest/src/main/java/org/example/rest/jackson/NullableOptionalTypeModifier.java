package org.example.rest.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import org.example.rest.util.NullableOptional;

import java.lang.reflect.Type;
import java.util.Optional;

public class NullableOptionalTypeModifier extends TypeModifier {

    @Override
    public JavaType modifyType(JavaType type, Type jdkType, TypeBindings context, TypeFactory typeFactory) {
        if (type.isReferenceType() || type.isContainerType()) {
            return type;
        }

        // we trick Jackson into thinking the type is NullableOptional<Optional<?>>
        if (type.isTypeOrSubTypeOf(NullableOptional.class)) {
            return ReferenceType.upgradeFrom(type, ReferenceType.upgradeFrom(typeFactory.constructType(Optional.class), type.containedTypeOrUnknown(0)));
        }

        return type;
    }
}
