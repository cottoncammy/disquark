package org.example.rest.request.codec;

import io.vertx.core.json.Json;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;
import org.example.rest.request.Request;

public class JsonCodec implements Codec {

    @Override
    public Body serialize(Request request, MultiMap headers) {
        return Body.from(Json.encode(request.body().get()));
    }

    @Override
    public <T> T deserialize(Buffer buffer, Class<T> type) {
        return buffer.toJsonObject().mapTo(type);
    }
}
