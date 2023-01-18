package org.example.rest.request;

import io.vertx.core.json.Json;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.http.HttpHeaders;

class JsonCodec implements Codec {

    @Override
    public Body serialize(Request request, MultiMap headers) {
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return Body.from(Json.encode(request.body().get()));
    }

    @Override
    public <T> T deserialize(Buffer buffer, Class<T> type) {
        if (type.isArray()) {
            return Json.decodeValue(buffer.getDelegate(), type);
        }
        return buffer.toJsonObject().mapTo(type);
    }
}
