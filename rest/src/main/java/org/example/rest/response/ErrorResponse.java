package org.example.rest.response;

import org.example.rest.immutables.ImmutableJson;

import java.util.Map;

@ImmutableJson
public interface ErrorResponse {

    int code();

    String message();

    Map<String, Object> errors();
}
