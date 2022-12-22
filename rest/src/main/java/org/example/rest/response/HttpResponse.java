package org.example.rest.response;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.codec.Codec;

import java.util.Map;

import static java.util.Objects.requireNonNull;

public class HttpResponse implements Response {
    private final Map<String, Codec> codecs;
    private final HttpClientResponse response;

    public HttpResponse(Map<String, Codec> codecs, HttpClientResponse response) {
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

    public HttpClientResponse getRaw() {
        return response;
    }
}
