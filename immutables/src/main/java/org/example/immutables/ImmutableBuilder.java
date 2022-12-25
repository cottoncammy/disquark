package org.example.immutables;

import org.immutables.value.Value.Style;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style.ImplementationVisibility;

import java.lang.annotation.*;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Style(
        of = "create",
        defaults = @Immutable(copy = false),
        visibility = ImplementationVisibility.PACKAGE,
        overshadowImplementation = true,
        depluralize = true,
        allMandatoryParameters = true
)
@Documented
public @interface ImmutableBuilder {
}
