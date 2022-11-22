package org.example.rest;

import io.vertx.mutiny.core.Vertx;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.Requester;
import org.example.rest.request.RequesterFactory;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;
import org.example.rest.response.Response;

import static java.util.Objects.requireNonNull;

public abstract class DiscordClient<T extends Response> {
    protected final Vertx vertx;
    protected final Requester<T> requester;

    protected DiscordClient(Vertx vertx, Requester<T> requester) {
        this.vertx = vertx;
        this.requester = requester;
    }

    public abstract static class Builder<R extends Response, T extends DiscordClient<R>> {
        protected Vertx vertx;
        protected RequesterFactory<R> requesterFactory;
        protected AccessTokenSource tokenSource;
        protected GlobalRateLimiter globalRateLimiter;
        protected RateLimitStrategy<R> rateLimitStrategy;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            this.vertx = vertx;
            this.tokenSource = tokenSource;
        }

        public Vertx getVertx() {
            return vertx;
        }

        public AccessTokenSource getTokenSource() {
            return tokenSource;
        }

        public Builder<R, T> requesterFactory(RequesterFactory<R> requesterFactory) {
            this.requesterFactory = requireNonNull(requesterFactory);
            return this;
        }

        public Builder<R, T> globalRateLimiter(GlobalRateLimiter globalRateLimiter) {
            this.globalRateLimiter = requireNonNull(globalRateLimiter);
            return this;
        }

        public GlobalRateLimiter getGlobalRateLimiter() {
            return globalRateLimiter;
        }

        public Builder<R, T> rateLimitStrategy(RateLimitStrategy<R> rateLimitStrategy) {
            this.rateLimitStrategy = requireNonNull(rateLimitStrategy);
            return this;
        }

        public RateLimitStrategy<R> getRateLimitStrategy() {
            return rateLimitStrategy;
        }

        public abstract T build();
    }
}
