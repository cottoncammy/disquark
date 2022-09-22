package org.example.rest.request.codec;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import org.example.rest.request.Request;

public interface Codec {

    Future<Void> serialize(RequestContext context);

    <T> T deserialize(Buffer buffer, Class<T> type);

    class RequestContext {
        private final Request request;
        private final HttpClientRequest httpClientRequest;

        public RequestContext(Request request, HttpClientRequest httpClientRequest) {
            this.request = request;
            this.httpClientRequest = httpClientRequest;
        }

        public Request getRequest() {
            return request;
        }

        public HttpClientRequest getHttpClientRequest() {
            return httpClientRequest;
        }
    }
}
