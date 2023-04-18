package io.disquark.rest.request;

import java.util.List;

import io.disquark.rest.json.Snowflake;
import io.disquark.rest.util.Tika;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.multipart.MultipartForm;

class MultipartCodec implements Codec {
    public static String CONTENT_TYPE = "multipart/form-data";

    private void addFile(MultipartForm form, String paramName, FileUpload file) {
        form.binaryFileUpload(paramName, file.getName(), file.getContent(), Tika.detect(file.getContent().getBytes()));
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public Body serialize(Request request, MultiMap headers) {
        MultipartForm form = MultipartForm.create();
        List<FileUpload> files = request.files();

        if (request.endpoint().getUriTemplate().getUri().equals("/guilds/{guild.id}/stickers")) {
            addFile(form, "file", files.get(0));
        } else {
            for (int i = 0; i < files.size(); i++) {
                FileUpload file = files.get(i);
                addFile(form, String.format("files[%d]", file.getId().map(Snowflake::getValue).orElse((long) i)), file);
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
