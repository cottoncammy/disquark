package org.example.rest.request;

import java.util.List;
import java.util.Map;

import io.netty.util.internal.StringUtil;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.multipart.MultipartForm;

import org.example.rest.util.Tika;

class MultipartCodec implements Codec {

    private void addFile(MultipartForm form, String paramName, Map.Entry<String, Buffer> file) {
        form.binaryFileUpload(paramName, file.getKey(), file.getValue(), Tika.detect(file.getValue().getBytes()));
    }

    @Override
    public Body serialize(Request request, MultiMap headers) {
        MultipartForm form = MultipartForm.create();
        List<Map.Entry<String, Buffer>> files = request.files();

        if (files.size() == 1) {
            addFile(form, "file", files.get(0));
        } else {
            for (int i = 0; i < files.size(); i++) {
                addFile(form, String.format("files[%d]", i), files.get(i));
            }
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
