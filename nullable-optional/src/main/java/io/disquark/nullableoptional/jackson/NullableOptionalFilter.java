package io.disquark.nullableoptional.jackson;

import javax.annotation.Nullable;

import io.disquark.nullableoptional.NullableOptional;

public class NullableOptionalFilter {

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || o.getClass() != NullableOptional.class) {
            return false;
        }
        NullableOptional<?> nullableOptional = (NullableOptional<?>) o;
        return !nullableOptional.isNull() && nullableOptional.toOptional().isEmpty();
    }
}
