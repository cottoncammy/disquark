package org.example.rest.request.codec;

import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.EncoderMode;
import io.vertx.ext.web.client.impl.MultipartFormUpload;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.streams.ReadStream;
import io.vertx.mutiny.ext.web.multipart.MultipartForm;
import org.example.rest.request.Request;

import java.util.List;
import java.util.Map;

public class MultipartCodec implements Codec {
    private final Vertx vertx;
    private final JsonCodec jsonCodec;

    public MultipartCodec(Vertx vertx, JsonCodec jsonCodec) {
        this.vertx = vertx;
        this.jsonCodec = jsonCodec;
    }

    @Override
    public Body serialize(Request request, MultiMap headers) {
        MultipartForm form = MultipartForm.create();
        List<Map.Entry<String, Buffer>> files = request.files().get();
        for (int i = 0; i < files.size(); i++) {
            Map.Entry<String, Buffer> file = files.get(i);
            form.binaryFileUpload(String.format("files[%d]", i), file.getKey(), file.getValue(), "application/octet-stream");
        }

        if (request.body().isPresent()) {
            form.attribute("payload_json", jsonCodec.serialize(request, headers).toString());
        }

        MultipartFormUpload upload;
        try {
            upload = new MultipartFormUpload(vertx.getOrCreateContext().getDelegate(), form.getDelegate(), true, EncoderMode.HTML5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        headers.addAll(MultiMap.newInstance(upload.headers()));
        return Body.from(ReadStream.newInstance(upload));
    }

    @Override
    public <T> T deserialize(Buffer buffer, Class<T> type) {
        throw new UnsupportedOperationException();
    }
}
