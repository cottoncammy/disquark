package org.example.rest.response;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.codec.Codec;

import java.util.Map;

// TODO make this an interface
public class Response {
    private final Map<String, Codec> codecs;
    private final HttpClientResponse response;

    public Response(Map<String, Codec> codecs, HttpClientResponse response) {
        this.codecs = codecs;
        this.response = response;
    }

    public <T> Uni<T> as(Class<T> type) {
        return response.body()
                .map(buffer -> {
                    Codec codec = requireNonNull(codecs.get(response.getHeader(HttpHeaders.CONTENT_TYPE)));
                    return codec.deserialize(buffer, type);
                });
    }

    public Uni<Void> skip() {
        return response.end();
    }

    public HttpClientResponse getHttpResponse() {
        return response;
    }
}
