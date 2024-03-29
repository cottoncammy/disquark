package io.disquark.rest.response;

import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = RateLimitResponse.class)
interface RateLimitResponseJson {

    String message();

    @JsonProperty("retry_after")
    float retryAfter();

    boolean global();

    OptionalInt code();
}
