package org.example.rest.request;

import static java.util.Objects.requireNonNull;
import static org.example.rest.response.DiscordException.isRetryableServerError;
import static org.example.rest.response.DiscordException.rateLimitIsExhausted;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.codec.Codec;
import org.example.rest.request.codec.JsonCodec;
import org.example.rest.request.codec.MultipartCodec;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.response.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

// TODO refactor the Requester interface (abstract class?)
// TODO generic headers transformer
public class HttpClientRequester implements Requester<HttpResponse> {
    private final String baseUrl;
    private final HttpClient httpClient;
    private final Map<String, Codec> codecs;
    private final AccessTokenSource tokenSource;
    private final GlobalRateLimiter rateLimiter;

    public static Builder builder(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        return new Builder(requireNonNull(vertx), requireNonNull(tokenSource), requireNonNull(rateLimiter));
    }

    public static HttpClientRequester create(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        return builder(vertx, tokenSource, rateLimiter).build();
    }

    protected HttpClientRequester(
            String baseUrl,
            HttpClient httpClient,
            Map<String, Codec> codecs,
            AccessTokenSource tokenSource,
            GlobalRateLimiter rateLimiter) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.tokenSource = tokenSource;
        this.rateLimiter = rateLimiter;
        this.codecs = codecs;
    }

    @Override
    public Uni<HttpResponse> request(Request request) {
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
                .onFailure(isRetryableServerError()).retry().withBackOff(Duration.ofSeconds(2), Duration.ofSeconds(30)).until(rateLimitIsExhausted());
    }

    public static class Builder {
        protected final Vertx vertx;
        protected final Map<String, Codec> codecs;
        protected final AccessTokenSource tokenSource;
        protected final GlobalRateLimiter rateLimiter;

        protected String baseUrl;
        protected HttpClient httpClient;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
            this.vertx = vertx;
            this.codecs = new HashMap<>();
            this.tokenSource = tokenSource;
            this.rateLimiter = rateLimiter;
        }

        public Builder baseUrl(String baseUrl) {
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

        public HttpClientRequester build() {
            codecs.putIfAbsent("application/json", new JsonCodec());
            codecs.putIfAbsent("multipart/form-data", new MultipartCodec(vertx, codecs.get("application/json")));

            return new HttpClientRequester(
                    baseUrl == null ? "https://discord.com/api/v10" : baseUrl,
                    httpClient == null ? vertx.createHttpClient(new HttpClientOptions().setSsl(true)) : httpClient,
                    codecs,
                    tokenSource,
                    rateLimiter);
        }
    }
}
