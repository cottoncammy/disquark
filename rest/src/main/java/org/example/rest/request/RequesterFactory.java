package org.example.rest.request;

import org.example.rest.DiscordClient;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;
import org.example.rest.response.HttpResponse;
import org.example.rest.response.Response;

import java.util.function.Function;

public interface RequesterFactory<T extends Response> extends Function<DiscordClient.Builder<T, ? extends DiscordClient<T>>, Requester<T>> {

    RequesterFactory<HttpResponse> HTTP_REQUESTER_FACTORY = new RequesterFactory<>() {
        @Override
        public Requester<HttpResponse> apply(DiscordClient.Builder<HttpResponse, ?> builder) {
            RateLimitStrategy<HttpResponse> rateLimitStrategy = builder.getRateLimitStrategy();
            if (rateLimitStrategy == null) {
                rateLimitStrategy = RateLimitStrategy.ALL;
            }

            GlobalRateLimiter globalRateLimiter = builder.getGlobalRateLimiter();
            if (globalRateLimiter == null) {
                globalRateLimiter = rateLimitStrategy.getGlobalRateLimiter();
            }
            return rateLimitStrategy.apply(HttpClientRequester.create(builder.getVertx(), builder.getTokenSource(), globalRateLimiter));
        }
    };
}
