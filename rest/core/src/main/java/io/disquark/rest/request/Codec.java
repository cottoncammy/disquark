package io.disquark.rest.request;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.concurrent.Flow;

import javax.annotation.Nullable;

import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;

public interface Codec {

    String getContentType();

    Body serialize(Request request, MultiMap headers);

    <T> T deserialize(Buffer buffer, Class<T> type);

    class Body {
        @Nullable
        private final String string;
        @Nullable
        private final Buffer buffer;
        @Nullable
        private final Flow.Publisher<Buffer> publisher;

        public static Body from(String body) {
            return new Body(requireNonNull(body, "body"), null, null);
        }

        public static Body from(Buffer body) {
            return new Body(null, requireNonNull(body, "body"), null);
        }

        public static Body from(Flow.Publisher<Buffer> body) {
            return new Body(null, null, requireNonNull(body, "body"));
        }

        private Body(String string, Buffer buffer, Flow.Publisher<Buffer> publisher) {
            this.string = string;
            this.buffer = buffer;
            this.publisher = publisher;
        }

        public Optional<String> asString() {
            return Optional.ofNullable(string);
        }

        public Optional<Buffer> asBuffer() {
            return Optional.ofNullable(buffer);
        }

        public Optional<Flow.Publisher<Buffer>> asPublisher() {
            return Optional.ofNullable(publisher);
        }

        public String getAsString() {
            if (string != null) {
                return string;
            }

            if (buffer != null) {
                return buffer.toString();
            }

            return "";
        }
    }
}
