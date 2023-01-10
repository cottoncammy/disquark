package org.example.rest.response;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Objects.requireNonNull;

public class HttpResponse implements Response {
    private static final Logger LOG = LoggerFactory.getLogger(HttpResponse.class);
    private final Map<String, Codec> codecs;
    private final HttpClientResponse response;

    public HttpResponse(Map<String, Codec> codecs, HttpClientResponse response) {
        this.codecs = codecs;
        this.response = response;
    }

    public <T> Uni<T> as(Class<T> type) {
        return response.body()
                .attachContext()
                .map(contextual -> {
                    String contentType = requireNonNull(response.getHeader(HttpHeaders.CONTENT_TYPE), "contentType");
                    Codec codec = requireNonNull(codecs.get(contentType), String.format("%s codec", contentType));
                    LOG.debug("Deserializing {} body for outgoing request {} as {}",
                            contentType, contextual.context().get("request-id"), type);
                    return codec.deserialize(contextual.get(), type);
                });
    }

    public HttpClientResponse getRaw() {
        return response;
    }

    public String getHeader(String headerName) {
        return response.getHeader(headerName);
    }
}
