package org.example.rest.interactions;

import io.vertx.core.json.Json;
import io.vertx.mutiny.core.buffer.Buffer;

public interface ServerCodec {

    ServerCodec DEFAULT_JSON_CODEC = new ServerCodec() {
        @Override
        public Buffer serialize(Object obj) {
            return Buffer.newInstance(Json.encodeToBuffer(obj));
        }

        @Override
        public <T> T deserialize(Buffer buffer, Class<T> type) {
            return buffer.toJsonObject().mapTo(type);
        }
    };

    Buffer serialize(Object obj);

    <T> T deserialize(Buffer buffer, Class<T> type);
}
