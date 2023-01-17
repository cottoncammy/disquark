package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;

import java.time.Duration;
import java.time.Instant;

public abstract class GlobalRateLimiter {
    protected volatile int retryAfter;

    public Uni<Void> setRetryAfter(int retryAfter) {
        return Uni.createFrom().voidItem().invoke(() -> this.retryAfter = retryAfter);
    }

    protected Uni<Duration> getRetryAfterDuration() {
        return Uni.createFrom().item(Duration.between(Instant.now(), Instant.now().plusSeconds(retryAfter)));
    }

    public abstract <T> Uni<T> rateLimit(Uni<T> upstream);
}
