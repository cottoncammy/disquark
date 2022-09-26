package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;

public class NoOpRateLimiter extends GlobalRateLimiter {

    @Override
    public <T> Uni<T> rateLimit(Uni<T> upstream) {
        return upstream;
    }
}
