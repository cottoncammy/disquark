package org.example.rest.request;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.multipart.MultipartForm;
import org.example.rest.response.Response;
import org.example.rest.response.VertxResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpClientRequester implements Requester {
    private final String baseUrl;
    private final HttpClient httpClient;
    private final AccessTokenSource tokenSource;

    public static Builder builder() {
        return new Builder();
    }

    // TODO modifiable request, retriable requests, response transformers
    protected HttpClientRequester(String baseUrl, HttpClient httpClient, AccessTokenSource tokenSource) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.tokenSource = tokenSource;
    }

    @Override
    public Future<Response> request(Requestable requestable, Context context) {
        Request request = requestable.asRequest();
        Endpoint endpoint = request.endpoint();

        return tokenSource.getToken()
                .compose(token -> httpClient.request(endpoint.getHttpMethod(), baseUrl, endpoint.getUriTemplate().expandToString(request.variables()))
                    .compose(req -> {
                        req.putHeader(HttpHeaders.AUTHORIZATION, String.format("%s %s", token.tokenType(), token.accessToken()));
                        req.putHeader(HttpHeaders.USER_AGENT, "TODO");

                        if (request.auditLogReason().isPresent()) {
                            req.putHeader("X-Audit-Log-Reason", request.auditLogReason().get());
                        }

                        if (request.contentType().isPresent()) {
                            String contentType = request.contentType().get();
                            req.putHeader(HttpHeaders.CONTENT_TYPE, contentType);

                            if (contentType.equals("multipart/form-data")) {
                                MultipartForm form = MultipartForm.create();
                                List<Map.Entry<String, Buffer>> files = request.files().get();
                                for (int i = 0; i < files.size(); i++) {
                                    Map.Entry<String, Buffer> file = files.get(i);
                                    form.binaryFileUpload(String.format("files[%d]", i), file.getKey(), file.getValue(), "application/octet-stream");
                                }

                                if (request.body().isPresent()) {
                                    form.attribute("payload_json", Json.encode(request.body().get()));
                                }
                                return req.send(MultipartWriter.write(form, context, req));
                            }
                            return req.send(Json.encode(request.body().get()));
                        }
                        return req.send();
                    }))
                .map(VertxResponse::new);
    }

    public static class Builder {
        protected String baseUrl;
        protected HttpClient httpClient;
        protected AccessTokenSource tokenSource;

        protected Builder() {}

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = Objects.requireNonNull(baseUrl);
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder tokenSource(AccessTokenSource tokenSource) {
            this.tokenSource = tokenSource;
            return this;
        }

        public HttpClientRequester build() {
            return new HttpClientRequester(baseUrl, Objects.requireNonNull(httpClient), Objects.requireNonNull(tokenSource));
        }
    }
}
