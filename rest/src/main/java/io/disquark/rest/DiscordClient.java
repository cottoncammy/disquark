package io.disquark.rest;

import static java.util.Objects.requireNonNull;

import io.disquark.rest.request.AccessTokenSource;
import io.disquark.rest.request.Requester;
import io.disquark.rest.request.RequesterFactory;
import io.disquark.rest.request.ratelimit.GlobalRateLimiter;
import io.disquark.rest.request.ratelimit.RateLimitStrategy;
import io.disquark.rest.response.Response;
import io.vertx.mutiny.core.Vertx;

// TODO https://stackoverflow.com/questions/3284610/returning-an-objects-subclass-with-generics
public abstract class DiscordClient<T extends Response> {
    protected final Vertx vertx;
    protected final Requester<T> requester;

    protected DiscordClient(Vertx vertx, Requester<T> requester) {
        this.vertx = vertx;
        this.requester = requester;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public Requester<T> getRequester() {
        return requester;
    }

    public abstract static class Builder<R extends Response, T extends DiscordClient<R>> {
        protected final Vertx vertx;
        protected final AccessTokenSource tokenSource;
        protected GlobalRateLimiter globalRateLimiter;
        protected RequesterFactory<R> requesterFactory;
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

        public Builder<R, T> globalRateLimiter(GlobalRateLimiter globalRateLimiter) {
            this.globalRateLimiter = requireNonNull(globalRateLimiter, "globalRateLimiter");
            return this;
        }

        public GlobalRateLimiter getGlobalRateLimiter() {
            return globalRateLimiter;
        }

        public Builder<R, T> requesterFactory(RequesterFactory<R> requesterFactory) {
            this.requesterFactory = requireNonNull(requesterFactory, "requesterFactory");
            return this;
        }

        @SuppressWarnings("unchecked")
        protected RequesterFactory<R> getRequesterFactory() {
            return requesterFactory == null ? (RequesterFactory<R>) RequesterFactory.DEFAULT_HTTP_REQUESTER : requesterFactory;
        }

        public Builder<R, T> rateLimitStrategy(RateLimitStrategy<R> rateLimitStrategy) {
            this.rateLimitStrategy = requireNonNull(rateLimitStrategy, "rateLimitStrategy");
            return this;
        }

        public RateLimitStrategy<R> getRateLimitStrategy() {
            return rateLimitStrategy;
        }

        public abstract T build();
    }
}
