package org.example.rest.request;

import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.EncoderMode;
import io.vertx.core.Context;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.ext.web.client.impl.MultipartFormUpload;
import io.vertx.ext.web.multipart.MultipartForm;

public class MultipartWriter {

    public static MultipartFormUpload write(MultipartForm body, Context context, HttpClientRequest request) {
        MultipartFormUpload multipartForm;
        try {
            multipartForm = new MultipartFormUpload(context, body, true, EncoderMode.HTML5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        multipartForm.headers().forEach(header -> request.putHeader(header.getKey(), header.getValue()));
        return multipartForm;
    }
}
