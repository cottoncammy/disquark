package org.example.immutables;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@NullableOptionalEncodingEnabled
@JsonSerialize
@Style(
        of = "create",
        defaults = @Immutable(copy = false),
        visibility = ImplementationVisibility.PACKAGE,
        overshadowImplementation = true,
        depluralize = true,
        allMandatoryParameters = true
)
@Documented
public @interface ImmutableJson {
}
