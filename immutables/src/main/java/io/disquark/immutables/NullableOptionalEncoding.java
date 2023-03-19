package io.disquark.immutables;

import java.util.Objects;

import io.disquark.nullableoptional.NullableOptional;

import org.immutables.encode.Encoding;

@Encoding
class NullableOptionalEncoding<T> {
    @Encoding.Impl
    private final NullableOptional<T> value = NullableOptional.empty();

    @Encoding.Expose
    NullableOptional<T> get() {
        return value;
    }

    @Encoding.Copy
    @Encoding.Naming(standard = Encoding.StandardNaming.WITH)
    NullableOptional<T> set(NullableOptional<T> value) {
        return Objects.requireNonNull(value);
    }

    @Encoding.Copy
    @Encoding.Naming(standard = Encoding.StandardNaming.WITH)
    NullableOptional<T> setUnwrapped(T value) {
        return NullableOptional.of(value);
    }
}
