package io.disquark.nullableoptional;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

public class NullableOptional<T> {
    @Nullable
    private final Optional<T> optional;

    public static <T> NullableOptional<T> of(T value) {
        return new NullableOptional<>(Optional.of(requireNonNull(value, "value")));
    }

    public static <T> NullableOptional<T> empty() {
        return new NullableOptional<>(Optional.empty());
    }

    public static <T> NullableOptional<T> ofNull() {
        return new NullableOptional<>(null);
    }

    private NullableOptional(Optional<T> optional) {
        this.optional = optional;
    }

    public boolean isNull() {
        return optional == null;
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(optional).flatMap(value -> value.or(Optional::empty));
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NullableOptional<?> that = (NullableOptional<?>) o;
        return Objects.equals(optional, that.optional);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(optional);
    }

    @Override
    public String toString() {
        if (optional == null) {
            return "NullableOptional.ofNull";
        }

        if (toOptional().isPresent()) {
            return String.format("NullableOptional[%s]", optional.get());
        }

        return "NullableOptional.empty";
    }
}
