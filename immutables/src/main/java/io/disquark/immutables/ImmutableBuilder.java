package io.disquark.immutables;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Target({ ElementType.PACKAGE, ElementType.TYPE })
@Style(of = "create", defaults = @Immutable(copy = false), visibility = ImplementationVisibility.PACKAGE, overshadowImplementation = true, depluralize = true, allMandatoryParameters = true)
@Documented
public @interface ImmutableBuilder {
}
