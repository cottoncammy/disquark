package org.example.rest.immutables;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@JsonSerialize
@ImmutableStyle
@Documented
public @interface ImmutableJson {
}
