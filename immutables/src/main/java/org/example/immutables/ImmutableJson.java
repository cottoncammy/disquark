package org.example.immutables;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style.ImplementationVisibility;

import java.lang.annotation.*;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@JsonSerialize
@Style(
        of = "create",
        defaults = @Immutable(copy = false),
        visibility = ImplementationVisibility.PACKAGE,
        optionalAcceptNullable = true,
        overshadowImplementation = true,
        depluralize = true,
        allMandatoryParameters = true
)
@Documented
public @interface ImmutableJson {
}
