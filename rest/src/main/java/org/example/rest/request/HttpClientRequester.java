package org.example.rest.request;

import static java.util.Objects.requireNonNull;
import static org.example.rest.response.DiscordException.isRetryableServerError;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.RequestOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.resources.oauth2.AccessToken;
import org.example.rest.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class HttpClientRequester implements Requester<HttpResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientRequester.class);
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

    @Override
    public Uni<HttpResponse> request(Request request) {
        Endpoint endpoint = request.endpoint();
        String id = Integer.toHexString(request.hashCode());

        LOG.debug("Preparing to send outgoing request {}: {}", id, request);
        return tokenSource.getToken()
                .attachContext()
                .flatMap(contextual -> {
                    RequestOptions options = new RequestOptions()
                            .setAbsoluteURI(baseUrl + endpoint.getUriTemplate().expandToString(request.variables()))
                            .setFollowRedirects(true)
                            .setMethod(endpoint.getHttpMethod())
                            .putHeader(HttpHeaders.USER_AGENT, String.format("DiscordBot (%s, %s)", "https://github.com/cameronprater/discord-TODO", "0.1.0"));

                    AccessToken token = contextual.get();
                    if (endpoint.isAuthenticationRequired()) {
                        options.putHeader(HttpHeaders.AUTHORIZATION, String.format("%s %s", token.tokenType().getValue(), token.accessToken()));
                    }

                    if (request.auditLogReason().isPresent()) {
                        options.putHeader("X-Audit-Log-Reason", request.auditLogReason().get());
                    }

                    contextual.context().put("request-id", id);

                    return rateLimiter.rateLimit(httpClient.request(options)).flatMap(req -> {
                        if (request.contentType().isPresent()) {
                            String contentType = request.contentType().get();
                            req.putHeader(HttpHeaders.CONTENT_TYPE, contentType);

                            Codec codec = requireNonNull(codecs.get(contentType), String.format("%s codec", contentType));
                            LOG.debug("Serializing {} body for outgoing request {}", contentType, id);
                            Codec.Body body = codec.serialize(request, req.headers());

                            LOG.debug("Sending outgoing request {}", id);
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

                        LOG.debug("Sending outgoing request {}", id);
                        return req.send();
                    });
                })
                .call(res -> {
                    if (Boolean.parseBoolean(res.getHeader("X-RateLimit-Global"))) {
                        String retryAfter = res.getHeader("Retry-After");
                        if (retryAfter != null) {
                            LOG.debug("Globally rate limited for the next PT{}S", retryAfter);
                            return rateLimiter.setRetryAfter(Math.round(Float.parseFloat(retryAfter)));
                        }
                        return Uni.createFrom().failure(IllegalStateException::new);
                    }
                    return Uni.createFrom().voidItem();
                })
                .map(res -> new HttpResponse(codecs, res))
                .call(response -> {
                    HttpClientResponse httpResponse = response.getRaw();
                    LOG.debug("Received {} - {} for outgoing request {}",
                            httpResponse.statusCode(), httpResponse.statusMessage(), id);

                    if (httpResponse.statusCode() == 429) {
                        return response.as(RateLimitResponse.class).onItem().failWith(res -> new RateLimitException(res, httpResponse));
                    } else if (httpResponse.statusCode() >= 400) {
                       return response.as(ErrorResponse.class).onItem().failWith(res -> new DiscordException(res, httpResponse));
                    }
                    return Uni.createFrom().voidItem();
                })
                .onFailure(isRetryableServerError()).retry().withBackOff(Duration.ofSeconds(2), Duration.ofSeconds(30)).atMost(5);
    }

    public static class Builder {
        protected final Vertx vertx;
        protected final Map<String, Codec> codecs;
        protected final AccessTokenSource tokenSource;
        protected final GlobalRateLimiter rateLimiter;

        protected URI baseUrl;
        protected HttpClient httpClient;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
            this.vertx = vertx;
            this.codecs = new HashMap<>();
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
            Codec jsonCodec = new JsonCodec();
            codecs.put("application/json", jsonCodec);
            codecs.put("multipart/form-data", new MultipartCodec(vertx, jsonCodec));

            return new HttpClientRequester(
                    baseUrl == null ? URI.create("https://discord.com/api/v10") : baseUrl,
                    httpClient == null ? vertx.createHttpClient() : httpClient,
                    codecs,
                    tokenSource,
                    rateLimiter);
        }
    }
}
