package io.disquark.immutables;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Target(ElementType.TYPE)
@Style(defaults = @Immutable(copy = false), visibility = ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@Documented
public @interface ImmutableBuilder {
}
