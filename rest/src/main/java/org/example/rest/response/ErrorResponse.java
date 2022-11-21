package org.example.rest.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;

import java.util.Map;

@ImmutableJson
@JsonDeserialize(as = ImmutableErrorResponse.class)
public interface ErrorResponse {

    int code();

    String message();

    Map<String, Object> errors();
}
