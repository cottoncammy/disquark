package org.example.rest.response;

import io.vertx.mutiny.core.http.HttpClientResponse;

import java.util.Arrays;
import java.util.function.Predicate;

import static org.example.rest.util.ExceptionPredicate.is;

public class DiscordException extends RuntimeException {
    private final HttpClientResponse httpResponse;
    private final ErrorResponse errorResponse;

    private static Predicate<Throwable> statusCodeIs(Integer... statusCode) {
        return is(DiscordException.class).and(t -> Arrays.asList(statusCode).contains(((DiscordException) t).httpResponse.statusCode()));
    }

    public static Predicate<Throwable> isRetryableServerError() {
        return statusCodeIs(500, 502, 503, 504, 520);
    }

    public static Predicate<Throwable> rateLimitIsExhausted() {
        return t -> {
            String remaining = ((DiscordException) t).httpResponse.headers().get("X-RateLimit-Remaining");
            return remaining == null || Integer.parseInt(remaining) == 0;
        };
    }

    public DiscordException(HttpClientResponse httpResponse, ErrorResponse errorResponse) {
        this.httpResponse = httpResponse;
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
