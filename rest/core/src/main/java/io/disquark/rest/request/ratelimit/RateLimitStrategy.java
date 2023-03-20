package io.disquark.rest.request.ratelimit;

import java.util.function.Function;

import io.disquark.rest.request.Requester;
import io.disquark.rest.response.HttpResponse;
import io.disquark.rest.response.Response;

public interface RateLimitStrategy<T extends Response> extends Function<Requester<T>, Requester<T>> {

    RateLimitStrategy<Response> GLOBAL = new RateLimitStrategy<>() {
        @Override
        public Requester<Response> apply(Requester<Response> requester) {
            return requester;
        }

        @Override
        public GlobalRateLimiter getGlobalRateLimiter() {
            return Bucket4jRateLimiter.create();
        }
    };

    RateLimitStrategy<HttpResponse> BUCKET = new RateLimitStrategy<>() {
        @Override
        public Requester<HttpResponse> apply(Requester<HttpResponse> requester) {
            return new BucketRateLimitingRequester(requester);
        }

        @Override
        public GlobalRateLimiter getGlobalRateLimiter() {
            return new NoOpRateLimiter();
        }
    };

    RateLimitStrategy<HttpResponse> ALL = new RateLimitStrategy<>() {
        @Override
        public Requester<HttpResponse> apply(Requester<HttpResponse> requester) {
            return new BucketRateLimitingRequester(requester);
        }

        @Override
        public GlobalRateLimiter getGlobalRateLimiter() {
            return Bucket4jRateLimiter.create();
        }
    };

    RateLimitStrategy<Response> NONE = new RateLimitStrategy<>() {
        @Override
        public Requester<Response> apply(Requester<Response> requester) {
            return requester;
        }

        @Override
        public GlobalRateLimiter getGlobalRateLimiter() {
            return new NoOpRateLimiter();
        }
    };

    GlobalRateLimiter getGlobalRateLimiter();
}
