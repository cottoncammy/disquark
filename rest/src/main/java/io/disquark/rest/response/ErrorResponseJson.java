package io.disquark.rest.response;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ErrorResponse.class)
interface ErrorResponseJson {

    int code();

    String message();

    Map<String, Object> errors();
}
