package org.example.rest.request.ratelimit;

import org.example.rest.request.Requester;

import java.util.function.Function;

public interface RateLimitStrategy extends Function<Requester, Requester> {

    RateLimitStrategy GLOBAL = new RateLimitStrategy() {
        @Override
        public Requester apply(Requester requester) {
            return requester;
        }

        @Override
        public GlobalRateLimiter getGlobalRateLimiter() {
            return Bucket4jRateLimiter.create();
        }
    };

    RateLimitStrategy BUCKET = new RateLimitStrategy() {
        @Override
        public Requester apply(Requester requester) {
            return new BucketRateLimitingRequester(requester);
        }

        @Override
        public GlobalRateLimiter getGlobalRateLimiter() {
            return new NoOpRateLimiter();
        }
    };

    RateLimitStrategy ALL = new RateLimitStrategy() {
        @Override
        public Requester apply(Requester requester) {
            return new BucketRateLimitingRequester(requester);
        }

        @Override
        public GlobalRateLimiter getGlobalRateLimiter() {
            return Bucket4jRateLimiter.create();
        }
    };

    RateLimitStrategy NONE = new RateLimitStrategy() {
        @Override
        public Requester apply(Requester requester) {
            return requester;
        }

        @Override
        public GlobalRateLimiter getGlobalRateLimiter() {
            return new NoOpRateLimiter();
        }
    };

    GlobalRateLimiter getGlobalRateLimiter();
}
