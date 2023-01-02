package org.example.rest;

import org.example.rest.request.HttpClientRequester;
import org.example.rest.request.Requester;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;
import org.example.rest.response.HttpResponse;
import org.example.rest.response.Response;

import java.util.function.Function;

public interface RequesterFactory<T extends Response> extends Function<DiscordClient.Builder<T, ? extends DiscordClient<T>>, Requester<T>> {

    static RequesterFactory<HttpResponse> customHttpRequester(Function<HttpClientRequester.Builder, HttpClientRequester> requesterBuilder) {
        return new RequesterFactory<HttpResponse>() {
            @Override
            public Requester<HttpResponse> apply(DiscordClient.Builder<HttpResponse, ? extends DiscordClient<HttpResponse>> builder) {
                RateLimitStrategy<HttpResponse> rateLimitStrategy = builder.getRateLimitStrategy();
                if (rateLimitStrategy == null) {
                    rateLimitStrategy = RateLimitStrategy.ALL;
                }

                GlobalRateLimiter globalRateLimiter = builder.getGlobalRateLimiter();
                if (globalRateLimiter == null) {
                    globalRateLimiter = rateLimitStrategy.getGlobalRateLimiter();
                }

                return rateLimitStrategy.apply(requesterBuilder.apply(HttpClientRequester.builder(builder.getVertx(), builder.getTokenSource(), globalRateLimiter)));
            }
        };
    }

    RequesterFactory<HttpResponse> DEFAULT_HTTP_REQUESTER = new RequesterFactory<>() {
        @Override
        public Requester<HttpResponse> apply(DiscordClient.Builder<HttpResponse, ? extends DiscordClient<HttpResponse>> builder) {
            return customHttpRequester(HttpClientRequester.Builder::build).apply(builder);
        }
    };
}
