package org.example.rest.request;

import io.netty.util.internal.StringUtil;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.multipart.MultipartForm;
import org.apache.tika.Tika;

import java.util.List;
import java.util.Map;

class MultipartCodec implements Codec {
    private static final Tika TIKA = new Tika();

    @Override
    public Body serialize(Request request, MultiMap headers) {
        MultipartForm form = MultipartForm.create();
        List<Map.Entry<String, Buffer>> files = request.files();
        for (int i = 0; i < files.size(); i++) {
            Map.Entry<String, Buffer> file = files.get(i);
            form.binaryFileUpload(String.format("files[%d]", i), file.getKey(), file.getValue(), TIKA.detect(file.getValue().getBytes()));
        }

        if (request.body().isPresent()) {
            form.textFileUpload("payload_json", StringUtil.EMPTY_STRING,
                    Buffer.buffer(Json.encode(request.body().get())), "application/json");
        }

        MultipartFormUpload upload;
        try {
            upload = new MultipartFormUpload(form);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        headers.addAll(upload.headers());
        return Body.from(upload);
    }

    @Override
    public <T> T deserialize(Buffer buffer, Class<T> type) {
        throw new UnsupportedOperationException();
    }
}
