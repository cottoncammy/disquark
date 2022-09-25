package org.example.rest.immutables;

import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;
import org.immutables.value.Value.Immutable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Style(
        defaults = @Immutable(copy = false),
        visibility = ImplementationVisibility.PACKAGE,
        overshadowImplementation = true,
        depluralize = true
)
@Documented
public @interface ImmutableStyle {
}
