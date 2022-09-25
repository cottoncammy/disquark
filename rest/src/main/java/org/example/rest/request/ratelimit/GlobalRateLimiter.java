package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;

import java.time.Duration;
import java.time.Instant;

public abstract class GlobalRateLimiter {
    protected volatile long retryAfter;

    public void setRetryAfter(long retryAfter) {
        this.retryAfter = retryAfter;
    }

    protected Duration getRetryAfterDuration() {
        return Duration.between(Instant.now(), Instant.ofEpochSecond(retryAfter));
    }

    public abstract <T> Uni<T> rateLimit(Uni<T> upstream);
}
