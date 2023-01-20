package org.example.rest.response;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ImmutableErrorResponse.class)
public interface ErrorResponse {

    int code();

    String message();

    Map<String, Object> errors();
}
