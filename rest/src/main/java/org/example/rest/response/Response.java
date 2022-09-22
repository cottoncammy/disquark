package org.example.rest.response;

import static java.util.Objects.requireNonNull;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpHeaders;
import org.example.rest.request.codec.Codec;

import java.util.Map;

public class Response {
    private final Map<String, Codec> codecs;
    private final HttpClientResponse response;

    public Response(Map<String, Codec> codecs, HttpClientResponse response) {
        this.codecs = codecs;
        this.response = response;
    }

    public <T> Future<T> as(Class<T> type) {
        Buffer buffer = Buffer.buffer();
        return response.handler(buffer::appendBuffer)
                .end()
                .map(x -> {
                    Codec codec = requireNonNull(codecs.get(response.getHeader(HttpHeaders.CONTENT_TYPE)));
                    if (response.statusCode() == 429) {
                        throw new RateLimitException(codec.deserialize(buffer, RateLimitResponse.class));
                    } else if (response.statusCode() >= 400) {
                        throw new DiscordException(codec.deserialize(buffer, ErrorResponse.class));
                    }
                    return codec.deserialize(buffer, type);
                });
    }
}
