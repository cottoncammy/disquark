package io.disquark.immutables;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Target(ElementType.TYPE)
@NullableOptionalEncodingEnabled
@JsonSerialize
@Style(of = "new", typeImmutable = "*Uni", defaults = @Immutable(builder = false), visibility = ImplementationVisibility.PUBLIC, optionalAcceptNullable = true, allMandatoryParameters = true)
@Documented
public @interface ImmutableUni {
}
