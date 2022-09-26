package org.example.rest.request;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.codec.Codec;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.response.Response;

import java.util.Map;

public class HttpClientRequester implements Requester {
    private final Vertx vertx;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final Map<String, Codec> codecs;
    private final AccessTokenSource tokenSource;
    // TODO authorized and unauthorized rate limiters
    private final GlobalRateLimiter rateLimiter;

    public static Builder builder(Vertx vertx) {
        return new Builder(requireNonNull(vertx));
    }

    protected HttpClientRequester(Vertx vertx, String baseUrl, HttpClient httpClient, Map<String, Codec> codecs, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        this.vertx = vertx;
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.codecs = codecs;
        this.tokenSource = tokenSource;
        this.rateLimiter = rateLimiter;
    }

    // TODO generic headers transformer
    @Override
    public Uni<Response> request(Request request) {
        Endpoint endpoint = request.endpoint();

        return tokenSource.getToken()
                .flatMap(token -> httpClient.request(endpoint.getHttpMethod(), baseUrl, endpoint.getUriTemplate().expandToString(request.variables()))
                    .flatMap(req -> {
                        req.putHeader(HttpHeaders.AUTHORIZATION, String.format("%s %s", token.tokenType(), token.accessToken()));
                        req.putHeader(HttpHeaders.USER_AGENT, "TODO");

                        if (request.auditLogReason().isPresent()) {
                            req.putHeader("X-Audit-Log-Reason", request.auditLogReason().get());
                        }

                        if (request.contentType().isPresent()) {
                            String contentType = request.contentType().get();
                            req.putHeader(HttpHeaders.CONTENT_TYPE, contentType);

                            Codec codec = requireNonNull(codecs.get(contentType));
                            Codec.Body body = codec.serialize(request, req.headers());
                            if (body.asString().isPresent()) {
                                return req.send(body.asString().get());
                            }

                            if (body.asBuffer().isPresent()) {
                                return req.send(body.asBuffer().get());
                            }

                            if (body.asReadStream().isPresent()) {
                                return req.send(body.asReadStream().get());
                            }

                            return req.send(body.asPublisher().get());
                        }

                        return req.send();
                    }))
                .call(res -> {
                    if (Boolean.parseBoolean(res.getHeader("X-RateLimit-Global"))) {
                        return rateLimiter.setRetryAfter(Math.round(Float.parseFloat(res.getHeader("Retry-After"))));
                    }
                    return Uni.createFrom().voidItem();
                })
                .map(res -> new Response(codecs, res));
    }

    public static class Builder {
        protected final Vertx vertx;

        protected String baseUrl;
        protected HttpClient httpClient;
        protected Map<String, Codec> codecs;
        protected AccessTokenSource tokenSource;
        protected GlobalRateLimiter rateLimiter;

        protected Builder(Vertx vertx) {
            this.vertx = vertx;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = requireNonNull(baseUrl);
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = requireNonNull(httpClient);
            return this;
        }

        public Builder codecs(Map<String, Codec> codecs) {
            this.codecs = requireNonNull(codecs);
            return this;
        }

        public Builder putCodec(String contentType, Codec codec) {
            codecs.put(contentType, requireNonNull(codec));
            return this;
        }

        public Builder tokenSource(AccessTokenSource tokenSource) {
            this.tokenSource = tokenSource;
            return this;
        }

        public Builder rateLimiter(GlobalRateLimiter rateLimiter) {
            this.rateLimiter = requireNonNull(rateLimiter);
            return this;
        }

        public HttpClientRequester build() {
            return new HttpClientRequester(vertx, baseUrl, httpClient, codecs, requireNonNull(tokenSource), rateLimiter);
        }
    }
}
