package org.example.rest.response;

import java.util.Optional;

import javax.annotation.Nullable;

import io.vertx.mutiny.core.http.HttpClientResponse;

public class RateLimitException extends RuntimeException {
    @Nullable
    private final Scope scope;
    @Nullable
    private final String bucket;
    private final RateLimitResponse response;
    private final HttpClientResponse httpResponse;

    public RateLimitException(RateLimitResponse response, HttpClientResponse httpResponse) {
        super(String.format("%s %s returned %s", httpResponse.request().getMethod(), httpResponse.request().getURI(),
                response));
        this.scope = getScope(httpResponse.getHeader("X-RateLimit-Scope"));
        this.bucket = httpResponse.getHeader("X-RateLimit-Bucket");
        this.response = response;
        this.httpResponse = httpResponse;
    }

    private Scope getScope(String value) {
        switch (value) {
            case "user":
                return Scope.USER;
            case "global":
                return Scope.GLOBAL;
            case "shared":
                return Scope.SHARED;
            default:
                return null;
        }
    }

    public Optional<Scope> getScope() {
        return Optional.ofNullable(scope);
    }

    public Optional<String> getBucket() {
        return Optional.ofNullable(bucket);
    }

    public RateLimitResponse getResponse() {
        return response;
    }

    public HttpClientResponse getHttpResponse() {
        return httpResponse;
    }

    public enum Scope {
        USER("user"),
        GLOBAL("global"),
        SHARED("shared");

        private final String value;

        Scope(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
