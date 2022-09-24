package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;

public interface GlobalRateLimiter {

    <T> Uni<T> rateLimit(Uni<T> stage);
}
