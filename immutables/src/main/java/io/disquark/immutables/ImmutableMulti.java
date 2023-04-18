package io.disquark.immutables;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Target(ElementType.TYPE)
@NullableOptionalEncodingEnabled
@JsonSerialize
@Style(of = "new", typeImmutable = "*Multi", visibility = ImplementationVisibility.PUBLIC, optionalAcceptNullable = true, allMandatoryParameters = true, copyMandatoryAttributes = false)
@Documented
public @interface ImmutableMulti {
}
