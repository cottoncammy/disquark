package org.example.rest.request.ratelimit;

import static io.smallrye.mutiny.unchecked.Unchecked.supplier;
import static java.util.Objects.requireNonNull;
import static org.example.rest.request.HttpClientRequester.FALLBACK_REQUEST_ID;
import static org.example.rest.request.HttpClientRequester.REQUEST_ID;
import static org.example.rest.util.ExceptionPredicate.is;
import static org.example.rest.util.ExceptionPredicate.wasCausedBy;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.smallrye.mutiny.Context;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Supplier;

public class Bucket4jRateLimiter extends GlobalRateLimiter {
    private static final Logger LOG = LoggerFactory.getLogger(Bucket4jRateLimiter.class);

    private final Bucket bucket;

    public static Bucket4jRateLimiter create(Bucket bucket) {
        return new Bucket4jRateLimiter(requireNonNull(bucket, "bucket"));
    }

    public static Bucket4jRateLimiter create(Bandwidth bandwidth) {
        return create(Bucket.builder().addLimit(requireNonNull(bandwidth, "bandwidth")).build());
    }

    public static Bucket4jRateLimiter create() {
        return create(Bandwidth.simple(50, Duration.ofSeconds(1)));
    }

    private Bucket4jRateLimiter(Bucket bucket) {
        this.bucket = bucket;
    }

    private <T> Uni<T> rateLimit(Uni<T> upstream, Context ctx) {
        LOG.debug("Acquiring bucket token for outgoing request {}, {} tokens available",
                ctx.getOrElse(REQUEST_ID, FALLBACK_REQUEST_ID), bucket.getAvailableTokens());

        return Uni.createFrom().item(supplier(() -> bucket.asBlocking().tryConsume(1, 10)))
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .call(consumed -> {
                    if (!consumed) {
                        return Uni.createFrom().failure(IllegalStateException::new);
                    }
                    return Uni.createFrom().voidItem();
                })
                .onFailure(is(RuntimeException.class).and(wasCausedBy(InterruptedException.class))).invoke(() -> {
                    LOG.warn("Thread interrupted while waiting to acquire bucket token for outgoing request {}",
                            ctx.getOrElse(REQUEST_ID, FALLBACK_REQUEST_ID));

                    bucket.addTokens(1);
                })
                .replaceWith(getRetryAfterDuration())
                .flatMap(retryAfterDuration -> {
                    if (!retryAfterDuration.isZero() && !retryAfterDuration.isNegative()) {
                        LOG.debug("Globally rate limited: delaying outgoing request {} by {}s",
                                ctx.getOrElse(REQUEST_ID, FALLBACK_REQUEST_ID), retryAfterDuration.getSeconds());

                        return Uni.createFrom().voidItem().onItem().delayIt().by(retryAfterDuration);
                    }
                    return Uni.createFrom().voidItem();
                })
                .replaceWith(upstream);
    }

    @Override
    public <T> Uni<T> rateLimit(Uni<T> upstream) {
        return Uni.createFrom().context(ctx -> rateLimit(upstream, ctx));
    }
}
