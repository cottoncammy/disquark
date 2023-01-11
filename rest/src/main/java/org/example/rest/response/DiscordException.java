package org.example.rest.response;

import io.vertx.mutiny.core.http.HttpClientResponse;

import java.util.Arrays;
import java.util.function.Predicate;

import static org.example.rest.util.ExceptionPredicate.is;

public class DiscordException extends RuntimeException {
    private final ErrorResponse errorResponse;
    private final HttpClientResponse httpResponse;

    public DiscordException(ErrorResponse errorResponse, HttpClientResponse httpResponse) {
        super(String.format("%s %s returned %s %s: %s",
                httpResponse.request().getMethod(), httpResponse.request().getURI(), httpResponse.statusCode(),
                httpResponse.statusMessage(), errorResponse));
        this.errorResponse = errorResponse;
        this.httpResponse = httpResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public HttpClientResponse getHttpResponse() {
        return httpResponse;
    }

    public boolean isStatusCode(Integer... statusCode) {
        return Arrays.asList(statusCode).contains(httpResponse.statusCode());
    }
}
