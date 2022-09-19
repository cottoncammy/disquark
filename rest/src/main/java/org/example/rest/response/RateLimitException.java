package org.example.rest.response;

public class RateLimitException extends RuntimeException {
    private final RateLimitResponse response;

    public RateLimitException(RateLimitResponse response) {
        this.response = response;
    }
}
