package org.example.rest.response;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;

public class VertxResponse implements Response {
    private final HttpClientResponse response;

    public VertxResponse(HttpClientResponse response) {
        this.response = response;
    }

    // TODO handle more response types, buffer the response body
    @Override
    public <T> Future<T> as(Class<T> type) {
        return response.body().map(Buffer::toJsonObject).map(json -> {
            if (response.statusCode() == 429) {
                throw new RateLimitException(json.mapTo(RateLimitResponse.class));
            } else if (response.statusCode() >= 400) {
                throw new DiscordException(json.mapTo(ErrorResponse.class));
            }
            return json.mapTo(type);
        });
    }
}
