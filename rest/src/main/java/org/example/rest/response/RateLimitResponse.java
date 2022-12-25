package org.example.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ImmutableRateLimitResponse.class)
public interface RateLimitResponse {

    String message();

    @JsonProperty("retry_after")
    float retryAfter();

    boolean global();
}
