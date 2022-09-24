package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;

public class Bucket4jRateLimiter implements GlobalRateLimiter {

    // seconds
    public void setRateLimitReset() {

    }

    // duration
    public void getRateLimitResetAfter() {

    }

    @Override
    public <T> Uni<T> rateLimit(Uni<T> stage) {
        return null;
    }
}
