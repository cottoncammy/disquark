package org.example.rest.response;

public class RateLimitException extends RuntimeException {
    private final RateLimitResponse response;
    private String scope;

    public RateLimitException(RateLimitResponse response) {
        super(response.message());
        this.response = response;
    }

    public RateLimitResponse getResponse() {
        return response;
    }

    public String getScope() {
        return scope;
    }
}
