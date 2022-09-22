package org.example.rest.request.codec;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;

public class JsonCodec implements Codec {

    @Override
    public Future<Void> serialize(RequestContext context) {
        return context.getHttpClientRequest().write(Json.encode(context.getRequest().body().get()));
    }

    @Override
    public <T> T deserialize(Buffer buffer, Class<T> type) {
        return buffer.toJsonObject().mapTo(type);
    }
}
