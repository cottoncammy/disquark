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
    NullableOptional<T> setValue(T value) {
        return NullableOptional.of(value);
    }

    @Encoding.Builder
    static class Builder<T> {
        private NullableOptional<T> value = NullableOptional.empty();

        @Encoding.Init
        @Encoding.Copy
        void set(NullableOptional<T> value) {
            // can't use static imports
            this.value = Objects.requireNonNull(value);
        }

        @Encoding.Init
        void setValue(T value) {
            this.value = NullableOptional.of(value);
        }

        @Encoding.Build
        NullableOptional<T> build() {
            return value;
        }
    }
}
