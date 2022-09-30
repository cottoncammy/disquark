package org.example.rest;

import io.vertx.mutiny.core.Vertx;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.Requester;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;

import static java.util.Objects.requireNonNull;

public abstract class DiscordClient {
    protected final Vertx vertx;
    protected final Requester requester;
    protected final AccessTokenSource tokenSource;
    protected final GlobalRateLimiter globalRateLimiter;

    // TODO we only need the tokenSource and globalRateLimiter for our AbstractRequester (which will be required instead of Requester?)
    protected DiscordClient(
            Vertx vertx,
            Requester requester,
            AccessTokenSource tokenSource,
            GlobalRateLimiter globalRateLimiter) {
        this.vertx = vertx;
        this.requester = requester;
        this.tokenSource = tokenSource;
        this.globalRateLimiter = globalRateLimiter;
    }

    protected abstract static class Builder<T extends DiscordClient> {
        protected Vertx vertx;
        protected Requester requester;
        protected AccessTokenSource tokenSource;
        protected GlobalRateLimiter globalRateLimiter;
        protected RateLimitStrategy rateLimitStrategy;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            this.vertx = vertx;
            this.tokenSource = tokenSource;
        }

        public Builder<T> requester(Requester requester) {
            this.requester = requireNonNull(requester);
            return this;
        }

        public Builder<T> globalRateLimiter(GlobalRateLimiter globalRateLimiter) {
            this.globalRateLimiter = requireNonNull(globalRateLimiter);
            return this;
        }

        public Builder<T> rateLimitStrategy(RateLimitStrategy rateLimitStrategy) {
            this.rateLimitStrategy = requireNonNull(rateLimitStrategy);
            return this;
        }

        public abstract T build();
    }
}
