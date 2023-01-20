package org.example.rest.request.ratelimit;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import io.smallrye.mutiny.Uni;

public abstract class GlobalRateLimiter {
    private volatile int ipRetryAfter;
    private volatile int appRetryAfter;

    public Uni<Void> setRetryAfter(int retryAfter, boolean app) {
        return Uni.createFrom().voidItem().invoke(() -> {
            if (app) {
                appRetryAfter = retryAfter;
            } else {
                ipRetryAfter = retryAfter;
            }
        });
    }

    protected Uni<Duration> getRetryAfterDuration(boolean app) {
        Supplier<Integer> retryAfter;
        if (app) {
            retryAfter = () -> appRetryAfter;
        } else {
            retryAfter = () -> ipRetryAfter;
        }

        return Uni.createFrom().item(Duration.between(Instant.now(), Instant.now().plusSeconds(retryAfter.get())));
    }

    public abstract <T> Uni<T> rateLimit(Uni<T> upstream, boolean app);
}
