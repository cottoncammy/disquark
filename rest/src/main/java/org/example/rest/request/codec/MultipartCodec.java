package org.example.rest.request.codec;

import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.EncoderMode;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.impl.MultipartFormUpload;
import io.vertx.ext.web.multipart.MultipartForm;

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
    public Future<Void> serialize(RequestContext context) {
        MultipartForm form = MultipartForm.create();
        List<Map.Entry<String, Buffer>> files = context.getRequest().files().get();
        for (int i = 0; i < files.size(); i++) {
            Map.Entry<String, Buffer> file = files.get(i);
            form.binaryFileUpload(String.format("files[%d]", i), file.getKey(), file.getValue(), "application/octet-stream");
        }

        if (context.getRequest().body().isPresent()) {
            form.attribute("payload_json", jsonCodec.serialize(context).toString());
        }

        MultipartFormUpload upload;
        try {
            upload = new MultipartFormUpload(vertx.getOrCreateContext(), form, true, EncoderMode.HTML5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        context.getHttpClientRequest().headers().addAll(upload.headers());
        return upload.pipeTo(context.getHttpClientRequest());
    }

    @Override
    public <T> T deserialize(Buffer buffer, Class<T> type) {
        throw new UnsupportedOperationException();
    }
}
