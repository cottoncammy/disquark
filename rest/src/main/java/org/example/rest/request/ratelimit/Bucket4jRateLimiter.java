package org.example.rest.request.ratelimit;

import static io.smallrye.mutiny.unchecked.Unchecked.supplier;
import static java.util.Objects.requireNonNull;
import static org.example.rest.util.ExceptionPredicate.is;
import static org.example.rest.util.ExceptionPredicate.wasCausedBy;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.time.Duration;
import java.util.function.Predicate;

public class Bucket4jRateLimiter extends GlobalRateLimiter {
    private final Bucket bucket;

    public static Bucket4jRateLimiter create(Bucket bucket) {
        return new Bucket4jRateLimiter(requireNonNull(bucket));
    }

    public static Bucket4jRateLimiter create(Bandwidth bandwidth) {
        return create(Bucket.builder().addLimit(requireNonNull(bandwidth)).build());
    }

    public static Bucket4jRateLimiter create() {
        return create(Bandwidth.simple(50, Duration.ofSeconds(1)));
    }

    private Bucket4jRateLimiter(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public <T> Uni<T> rateLimit(Uni<T> upstream) {
        return Multi.createBy().repeating().supplier(supplier(() -> bucket.asBlocking().tryConsume(1, 10)))
                .whilst(Predicate.isEqual(false))
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .onFailure(is(RuntimeException.class).and(wasCausedBy(InterruptedException.class))).invoke(() -> bucket.addTokens(1))
                .filter(Predicate.isEqual(true))
                .onItem().ignoreAsUni()
                .replaceWith(getRetryAfterDuration().flatMap())
                .onItem().delayIt().by(getRetryAfterDuration())
                .replaceWith(upstream);
    }
}
