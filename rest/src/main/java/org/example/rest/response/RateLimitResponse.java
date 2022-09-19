package org.example.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.immutables.ImmutableJson;

@ImmutableJson
public interface RateLimitResponse {

    String message();

    @JsonProperty("retry_after")
    float retryAfter();

    boolean global();
}
