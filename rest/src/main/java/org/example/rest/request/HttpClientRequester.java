package org.example.rest.request;

import static java.util.Objects.requireNonNull;
import static org.example.rest.response.DiscordException.isRetryableServerError;
import static org.example.rest.response.DiscordException.rateLimitIsExhausted;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.RequestOptions;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.core.http.HttpClientRequest;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.codec.Codec;
import org.example.rest.request.codec.JsonCodec;
import org.example.rest.request.codec.MultipartCodec;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.response.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HttpClientRequester implements Requester<HttpResponse> {
    private final URI baseUrl;
    private final HttpClient httpClient;
    private final Map<String, Codec> codecs;
    private final AccessTokenSource tokenSource;
    private final GlobalRateLimiter rateLimiter;
    private final Consumer<MultiMap> headersTransformer;

    public static Builder builder(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        return new Builder(requireNonNull(vertx), requireNonNull(tokenSource), requireNonNull(rateLimiter));
    }

    public static HttpClientRequester create(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        return builder(vertx, tokenSource, rateLimiter).build();
    }

    protected HttpClientRequester(
            URI baseUrl,
            HttpClient httpClient,
            Map<String, Codec> codecs,
            AccessTokenSource tokenSource,
            GlobalRateLimiter rateLimiter,
            Consumer<MultiMap> headersTransformer) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.codecs = codecs;
        this.tokenSource = tokenSource;
        this.rateLimiter = rateLimiter;
        this.headersTransformer = headersTransformer;
    }

    @Override
    public Uni<HttpResponse> request(Request request) {
        Endpoint endpoint = request.endpoint();

        // TODO handle webhook and interactions clients, don't need access token && need separate rate limiter
        return tokenSource.getToken()
                .flatMap(token -> {
                    RequestOptions options = new RequestOptions()
                            .setAbsoluteURI(baseUrl + endpoint.getUriTemplate().expandToString(request.variables()))
                            .setFollowRedirects(true)
                            .setMethod(endpoint.getHttpMethod());

                    return rateLimiter.rateLimit(httpClient.request(options)).flatMap(req -> {
                        req.putHeader(HttpHeaders.AUTHORIZATION, String.format("%s %s", token.tokenType().getValue(), token.accessToken()));
                        req.putHeader(HttpHeaders.USER_AGENT, String.format("DiscordBot (%s, %s)", "https://github.com/cameronprater/discord-TODO", "0.1.0"));

                        if (request.auditLogReason().isPresent()) {
                            req.putHeader("X-Audit-Log-Reason", request.auditLogReason().get());
                        }

                        if (request.contentType().isPresent()) {
                            String contentType = request.contentType().get();
                            req.putHeader(HttpHeaders.CONTENT_TYPE, contentType);

                            Codec codec = requireNonNull(codecs.get(contentType));
                            Codec.Body body = codec.serialize(request, req.headers());
                            headersTransformer.accept(req.headers());

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

                        headersTransformer.accept(req.headers());
                        return req.send();
                    });
                })
                .call(res -> {
                    if (Boolean.parseBoolean(res.getHeader("X-RateLimit-Global"))) {
                        return rateLimiter.setRetryAfter(Math.round(Float.parseFloat(res.getHeader("Retry-After"))));
                    }
                    return Uni.createFrom().voidItem();
                })
                .map(res -> new HttpResponse(codecs, res))
                .call(response -> {
                    HttpClientResponse httpResponse = response.getRaw();
                    if (httpResponse.statusCode() == 429) {
                        return response.as(RateLimitResponse.class).onItem().failWith(RateLimitException::new);
                    } else if (httpResponse.statusCode() >= 400) {
                       return response.as(ErrorResponse.class).onItem().failWith(res -> new DiscordException(httpResponse, res));
                    }
                    return Uni.createFrom().voidItem();
                })
                .onFailure(isRetryableServerError()).retry().until(rateLimitIsExhausted());
    }

    public static class Builder {
        protected final Vertx vertx;
        protected final Map<String, Codec> codecs;
        protected final AccessTokenSource tokenSource;
        protected final GlobalRateLimiter rateLimiter;

        protected URI baseUrl;
        protected HttpClient httpClient;
        protected Consumer<MultiMap> headersTransformer;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
            this.vertx = vertx;
            this.codecs = new HashMap<>();
            this.tokenSource = tokenSource;
            this.rateLimiter = rateLimiter;
        }

        public Builder baseUrl(URI baseUrl) {
            this.baseUrl = requireNonNull(baseUrl);
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = requireNonNull(httpClient);
            return this;
        }

        public Builder putCodec(String contentType, Codec codec) {
            codecs.put(contentType, requireNonNull(codec));
            return this;
        }

        public Builder putCodecs(Map<String, Codec> codecs) {
            this.codecs.putAll(codecs);
            return this;
        }

        public Builder headersTransformer(Consumer<MultiMap> headersTransformer) {
            this.headersTransformer = requireNonNull(headersTransformer);
            return this;
        }

        public HttpClientRequester build() {
            codecs.putIfAbsent("application/json", new JsonCodec());
            codecs.putIfAbsent("multipart/form-data", new MultipartCodec(vertx, codecs.get("application/json")));

            return new HttpClientRequester(
                    baseUrl == null ? URI.create("https://discord.com/api/v10") : baseUrl,
                    httpClient == null ? vertx.createHttpClient() : httpClient,
                    codecs,
                    tokenSource,
                    rateLimiter,
                    headersTransformer == null ? x -> {} : headersTransformer);
        }
    }
}
