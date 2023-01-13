package org.example.nullableoptional.jackson;

import org.example.nullableoptional.NullableOptional;

public class NullableOptionalFilter {

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != NullableOptional.class) return false;
        NullableOptional<?> nullableOptional = (NullableOptional<?>) o;
        return !nullableOptional.isNull() && nullableOptional.toOptional().isEmpty();
    }
}
