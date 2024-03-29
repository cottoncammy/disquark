package io.disquark.rest.kotlin.nullableoptional

import io.disquark.nullableoptional.NullableOptional
import java.util.Optional

fun <T> Optional<T>?.toNullableOptional(): NullableOptional<T> =
    when {
        this == null -> NullableOptional.ofNull()
        isEmpty -> NullableOptional.empty()
        else -> NullableOptional.of(get())
    }

fun <T> NullableOptional<T>.getOrNull(): T? = if (isNull) null else toOptional().get()
