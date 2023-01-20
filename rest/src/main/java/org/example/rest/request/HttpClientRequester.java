package org.example.rest.request;

import static java.util.Objects.requireNonNull;

import io.smallrye.mutiny.Context;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.core.http.HttpClientRequest;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.response.*;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HttpClientRequester implements Requester<HttpResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientRequester.class);

    public static final String REQUEST_ID = "request-id";
    public static final Supplier<String> FALLBACK_REQUEST_ID = () -> "NULL";

    private final URI baseUrl;
    private final HttpClient httpClient;
    private final Map<String, Codec> codecs;
    private final AccessTokenSource tokenSource;
    private final GlobalRateLimiter rateLimiter;

    public static Builder builder(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        return new Builder(requireNonNull(vertx, "vertx"), requireNonNull(tokenSource, "tokenSource"), requireNonNull(rateLimiter, "rateLimiter"));
    }

    public static HttpClientRequester create(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        return builder(vertx, tokenSource, rateLimiter).build();
    }

    protected HttpClientRequester(
            URI baseUrl,
            HttpClient httpClient,
            Map<String, Codec> codecs,
            AccessTokenSource tokenSource,
            GlobalRateLimiter rateLimiter) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.codecs = codecs;
        this.tokenSource = tokenSource;
        this.rateLimiter = rateLimiter;
    }

    private Uni<HttpResponse> request(Request request, Context ctx) {
        if (LOG.isDebugEnabled()) {
            ctx.put(REQUEST_ID, Integer.toHexString(request.hashCode()));
        }

        boolean authentication = request.endpoint().isAuthenticationRequired();

        return tokenSource.getToken()
                .flatMap(token -> {
                    LOG.debug("Preparing to send outgoing request {} as {}", request, ctx.get(REQUEST_ID));
                    RequestOptions options = new RequestOptions()
                            .setAbsoluteURI(baseUrl + request.endpoint().getUriTemplate().expandToString(request.variables()))
                            .setFollowRedirects(true)
                            .setMethod(request.endpoint().getHttpMethod())
                            .putHeader(HttpHeaders.USER_AGENT, String.format("DiscordBot (%s, %s)", "https://github.com/cameronprater/discord-TODO", "0.1.0"));

                    if (authentication) {
                        options.putHeader(HttpHeaders.AUTHORIZATION, String.format("%s %s", token.tokenType().getValue(), token.accessToken()));
                    }

                    if (request.auditLogReason().isPresent()) {
                        options.putHeader("X-Audit-Log-Reason", request.auditLogReason().get());
                    }

                    return rateLimiter.rateLimit(httpClient.request(options), authentication).flatMap(req -> {
                        if (request.contentType().isEmpty()) {
                            req.putHeader(HttpHeaders.CONTENT_LENGTH, "0");

                            LOG.debug("Sending outgoing request {}", ctx.<Object>get(REQUEST_ID));
                            return req.send();
                        }

                        String contentType = request.contentType().get();
                        Codec codec = requireNonNull(codecs.get(contentType), String.format("%s codec", contentType));
                        LOG.debug("Serializing {} body for outgoing request {}", contentType, ctx.get(REQUEST_ID));

                        return Uni.createFrom().item(codec.serialize(request, req.headers()))
                                .flatMap(body -> {
                                    LOG.debug("Sending outgoing request {} with body {}",
                                            ctx.get(REQUEST_ID), body.getAsString());

                                    if (body.asString().isPresent()) {
                                        return req.send(body.asString().get());
                                    }

                                    if (body.asBuffer().isPresent()) {
                                        return req.send(body.asBuffer().get());
                                    }

                                    return req.send(body.asPublisher().get());
                                });
                    });
                })
                .call(res -> {
                    if (Boolean.parseBoolean(res.getHeader("X-RateLimit-Global"))) {
                        String retryAfter = res.getHeader("Retry-After");
                        if (retryAfter != null) {
                            LOG.debug("{} requests will be globally rate limited for the next {}s",
                                    authentication ? "Authenticated" : "Unauthenticated", retryAfter);

                            return rateLimiter.setRetryAfter(Math.round(Float.parseFloat(retryAfter)), authentication)
                                    .onFailure(NumberFormatException.class).transform(IllegalStateException::new);
                        }
                        return Uni.createFrom().failure(IllegalStateException::new);
                    }
                    return Uni.createFrom().voidItem();
                })
                .map(res -> new HttpResponse(ctx.getOrElse(REQUEST_ID, FALLBACK_REQUEST_ID), codecs, res))
                .call(response -> {
                    HttpClientResponse httpResponse = response.getRaw();
                    LOG.debug("Received {} - {} for outgoing request {}",
                            httpResponse.statusCode(), httpResponse.statusMessage(), ctx.get(REQUEST_ID));
                    LOG.trace("Response headers for outgoing request {}: {}",
                            ctx.get(REQUEST_ID), httpResponse.headers().entries());

                    if (httpResponse.statusCode() == 429) {
                        return response.as(RateLimitResponse.class).onItem().failWith(res -> new RateLimitException(res, httpResponse));
                    } else if (httpResponse.statusCode() >= 400) {
                        return response.as(ErrorResponse.class).onItem().failWith(res -> new DiscordException(res, httpResponse));
                    }
                    return Uni.createFrom().voidItem();
                });
    }

    @Override
    public Uni<HttpResponse> request(Request request) {
        return Uni.createFrom().context(ctx -> request(request, ctx));
    }

    public static class Builder {
        protected final Vertx vertx;
        protected final Map<String, Codec> codecs = new HashMap<>();
        protected final AccessTokenSource tokenSource;
        protected final GlobalRateLimiter rateLimiter;

        protected URI baseUrl;
        protected HttpClient httpClient;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
            this.vertx = vertx;
            this.tokenSource = tokenSource;
            this.rateLimiter = rateLimiter;
        }

        public Builder baseUrl(URI baseUrl) {
            this.baseUrl = requireNonNull(baseUrl, "baseUrl");
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = requireNonNull(httpClient, "httpClient");
            return this;
        }

        public Builder putCodec(String contentType, Codec codec) {
            codecs.put(requireNonNull(contentType, "contentType"), requireNonNull(codec, "codec"));
            return this;
        }

        public Builder putCodecs(Map<String, Codec> codecs) {
            this.codecs.putAll(requireNonNull(codecs, "codecs"));
            return this;
        }

        public HttpClientRequester build() {
            codecs.put("application/json", new JsonCodec());
            codecs.put("multipart/form-data", new MultipartCodec());

            return new HttpClientRequester(
                    baseUrl == null ? URI.create("https://discord.com/api/v10") : baseUrl,
                    httpClient == null ? vertx.createHttpClient() : httpClient,
                    codecs,
                    tokenSource,
                    rateLimiter);
        }
    }
}
