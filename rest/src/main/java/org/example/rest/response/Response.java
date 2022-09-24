package org.example.rest.response;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.codec.Codec;

import java.util.Map;

public class Response {
    private final Map<String, Codec> codecs;
    private final HttpClientResponse response;

    public Response(Map<String, Codec> codecs, HttpClientResponse response) {
        this.codecs = codecs;
        this.response = response;
    }

    public <T> Uni<T> as(Class<T> type) {
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

    public Uni<Void> skip() {
        return response.end();
    }
}
