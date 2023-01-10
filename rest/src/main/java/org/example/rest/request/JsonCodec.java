package org.example.rest.request;

import io.vertx.core.json.Json;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;

// TODO logs
class JsonCodec implements Codec {

    @Override
    public Body serialize(Request request, MultiMap headers) {
        return Body.from(Json.encode(request.body().get()));
    }

    @Override
    public <T> T deserialize(Buffer buffer, Class<T> type) {
        return buffer.toJsonObject().mapTo(type);
    }
}
