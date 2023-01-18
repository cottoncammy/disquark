package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;

class NoOpRateLimiter extends GlobalRateLimiter {

    @Override
    public Uni<Void> setRetryAfter(int retryAfter, boolean app) {
        return Uni.createFrom().voidItem();
    }

    @Override
    public <T> Uni<T> rateLimit(Uni<T> upstream, boolean app) {
        return upstream;
    }
}
