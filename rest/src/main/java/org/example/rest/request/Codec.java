package org.example.rest.request;

import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.streams.ReadStream;
import org.reactivestreams.Publisher;

import javax.annotation.Nullable;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface Codec {

    Body serialize(Request request, MultiMap headers);

    <T> T deserialize(Buffer buffer, Class<T> type);

    class Body {
        @Nullable
        private final String string;
        @Nullable
        private final Buffer buffer;
        @Nullable
        private final ReadStream<Buffer> readStream;
        @Nullable
        private final Publisher<Buffer> publisher;

        public static Body from(String body) {
            return new Body(requireNonNull(body, "body"), null, null, null);
        }

        public static Body from(Buffer body) {
            return new Body(null, requireNonNull(body, "body"), null, null);
        }

        public static Body from(ReadStream<Buffer> body) {
            return new Body(null, null, requireNonNull(body, "body"), null);
        }

        public static Body from(Publisher<Buffer> body) {
            return new Body(null, null, null, requireNonNull(body, "body"));
        }

        private Body(String string, Buffer buffer, ReadStream<Buffer> readStream, Publisher<Buffer> publisher) {
            this.string = string;
            this.buffer = buffer;
            this.readStream = readStream;
            this.publisher = publisher;
        }

        public Optional<String> asString() {
            return Optional.ofNullable(string);
        }

        public Optional<Buffer> asBuffer() {
            return Optional.ofNullable(buffer);
        }

        public Optional<ReadStream<Buffer>> asReadStream() {
            return Optional.ofNullable(readStream);
        }

        public Optional<Publisher<Buffer>> asPublisher() {
            return Optional.ofNullable(publisher);
        }

        public String getAsString() {
            if (string != null) {
                return string;
            }

            if (buffer != null) {
                return buffer.toString();
            }

            return "TODO";
        }
    }
}
