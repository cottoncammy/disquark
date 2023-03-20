package io.disquark.rest.request;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import io.disquark.rest.DiscordClient;
import io.disquark.rest.request.ratelimit.GlobalRateLimiter;
import io.disquark.rest.request.ratelimit.RateLimitStrategy;
import io.disquark.rest.response.HttpResponse;
import io.disquark.rest.response.Response;

public interface RequesterFactory<T extends Response>
        extends Function<DiscordClient.Builder<T, ? extends DiscordClient<T>>, Requester<T>> {

    static RequesterFactory<HttpResponse> customHttpRequester(
            Function<HttpClientRequester.Builder, HttpClientRequester> httpRequesterFactory) {
        return new RequesterFactory<>() {
            @Override
            public Requester<HttpResponse> apply(
                    DiscordClient.Builder<HttpResponse, ? extends DiscordClient<HttpResponse>> builder) {
                RateLimitStrategy<HttpResponse> rateLimitStrategy = builder.getRateLimitStrategy();
                if (rateLimitStrategy == null) {
                    rateLimitStrategy = RateLimitStrategy.ALL;
                }

                GlobalRateLimiter globalRateLimiter = builder.getGlobalRateLimiter();
                if (globalRateLimiter == null) {
                    globalRateLimiter = rateLimitStrategy.getGlobalRateLimiter();
                }

                return rateLimitStrategy.apply(requireNonNull(httpRequesterFactory, "httpRequesterFactory")
                        .apply(HttpClientRequester.builder(builder.getVertx(), builder.getTokenSource(), globalRateLimiter)));
            }
        };
    }

    RequesterFactory<HttpResponse> DEFAULT_HTTP_REQUESTER = new RequesterFactory<>() {
        @Override
        public Requester<HttpResponse> apply(
                DiscordClient.Builder<HttpResponse, ? extends DiscordClient<HttpResponse>> builder) {
            return customHttpRequester(HttpClientRequester.Builder::build).apply(builder);
        }
    };
}
