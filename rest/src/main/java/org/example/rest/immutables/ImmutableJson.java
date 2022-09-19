package org.example.rest.immutables;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ImplementationVisibility;
import org.immutables.value.Value.Immutable;

import java.lang.annotation.*;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@JsonSerialize
@JsonInclude(Include.NON_ABSENT)
@Style(
        defaults = @Immutable(copy = false),
        visibility = ImplementationVisibility.PACKAGE,
        overshadowImplementation = true,
        depluralize = true
)
@Documented
public @interface ImmutableJson {
}
