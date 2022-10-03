package org.example.rest.request.ratelimit;

import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;
import org.example.rest.response.Response;

import java.util.function.Function;

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
