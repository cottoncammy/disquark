package io.disquark.rest.request;

import static io.disquark.rest.util.Logger.log;
import static io.smallrye.mutiny.unchecked.Unchecked.consumer;
import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Properties;
import java.util.function.Supplier;

import io.disquark.rest.request.ratelimit.GlobalRateLimiter;
import io.disquark.rest.response.DiscordException;
import io.disquark.rest.response.ErrorResponse;
import io.disquark.rest.response.HttpResponse;
import io.disquark.rest.response.RateLimitException;
import io.disquark.rest.response.RateLimitResponse;
import io.smallrye.mutiny.Context;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.RequestOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.core.http.HttpClientResponse;
import io.vertx.mutiny.core.http.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class HttpClientRequester implements Requester<HttpResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientRequester.class);

    public static final String REQUEST_ID = "request-id";
    public static final Supplier<String> FALLBACK_REQUEST_ID = () -> "NULL";

    private final URI baseUrl;
    private final HttpClient httpClient;
    private final AccessTokenSource tokenSource;
    private final GlobalRateLimiter rateLimiter;

    public static Builder builder(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        return new Builder(requireNonNull(vertx, "vertx"), requireNonNull(tokenSource, "tokenSource"),
                requireNonNull(rateLimiter, "rateLimiter"));
    }

    public static HttpClientRequester create(Vertx vertx, AccessTokenSource tokenSource, GlobalRateLimiter rateLimiter) {
        return builder(vertx, tokenSource, rateLimiter).build();
    }

    protected HttpClientRequester(
            URI baseUrl,
            HttpClient httpClient,
            AccessTokenSource tokenSource,
            GlobalRateLimiter rateLimiter) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.tokenSource = tokenSource;
        this.rateLimiter = rateLimiter;
    }

    private Uni<String> getVersion() {
        return Uni.createFrom().item(new Properties())
                .invoke(consumer(props -> props.load(getClass().getClassLoader().getResourceAsStream("maven.properties"))))
                .map(props -> props.getProperty("project.version"));
    }

    private Uni<HttpResponse> request(Request request, Context ctx) {
        if (LOG.isDebugEnabled()) {
            ctx.put(REQUEST_ID, Integer.toHexString(request.hashCode()));
        }

        boolean authentication = request.endpoint().isAuthenticationRequired();
        return Uni.combine().all().unis(tokenSource.getToken(), getVersion()).asTuple()
                .flatMap(tuple -> {
                    log(LOG, Level.DEBUG, log -> log.debug("Preparing to send outgoing request {} as {}",
                            request, ctx.get(REQUEST_ID)));

                    RequestOptions options = new RequestOptions()
                            .setAbsoluteURI(baseUrl + request.endpoint().getUriTemplate().expandToString(request.variables()))
                            .setFollowRedirects(true)
                            .setMethod(request.endpoint().getHttpMethod())
                            .putHeader(HttpHeaders.USER_AGENT, String.format("DiscordBot (%s, %s)",
                                    "https://github.com/disquark/disquark", tuple.getItem2()));

                    if (authentication) {
                        options.putHeader(HttpHeaders.AUTHORIZATION, String.format("%s %s",
                                tuple.getItem1().tokenType().getValue(), tuple.getItem1().accessToken()));
                    }

                    if (request.auditLogReason().isPresent()) {
                        options.putHeader("X-Audit-Log-Reason", request.auditLogReason().get());
                    }

                    return rateLimiter.rateLimit(httpClient.request(options), authentication).flatMap(req -> {
                        if (request.contentType().isEmpty()) {
                            req.putHeader(HttpHeaders.CONTENT_LENGTH, "0");
                            log(LOG, Level.DEBUG, log -> log.debug("Sending outgoing request {}",
                                    ctx.<Object> get(REQUEST_ID)));

                            return req.send();
                        }

                        String contentType = request.contentType().get();
                        Codec codec = Codecs.getCodec(contentType);
                        log(LOG, Level.DEBUG, log -> log.debug("Serializing {} body for outgoing request {}",
                                contentType, ctx.get(REQUEST_ID)));

                        return Uni.createFrom().item(codec.serialize(request, req.headers()))
                                .flatMap(body -> {
                                    log(LOG, Level.DEBUG, log -> log.debug("Sending outgoing request {} with body {}",
                                            ctx.get(REQUEST_ID), body.getAsString()));

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
                .map(res -> new HttpResponse(ctx.getOrElse(REQUEST_ID, FALLBACK_REQUEST_ID), res))
                .call(response -> {
                    HttpClientResponse httpResponse = response.getRaw();
                    log(LOG, Level.DEBUG, log -> log.debug("Received {} - {} for outgoing request {}",
                            httpResponse.statusCode(), httpResponse.statusMessage(), ctx.get(REQUEST_ID)));
                    log(LOG, Level.TRACE, log -> log.trace("Response headers for outgoing request {}: {}",
                            ctx.get(REQUEST_ID), httpResponse.headers().entries()));

                    if (httpResponse.statusCode() == 429) {
                        return response.as(RateLimitResponse.class)
                                .onItem().failWith(res -> new RateLimitException(res, httpResponse));
                    } else if (httpResponse.statusCode() >= 400) {
                        return response.as(ErrorResponse.class)
                                .onItem().failWith(res -> new DiscordException(res, httpResponse));
                    }
                    return Uni.createFrom().voidItem();
                });
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public Uni<HttpResponse> request(Request request) {
        return Uni.createFrom().context(ctx -> request(request, ctx));
    }

    public static class Builder {
        protected final Vertx vertx;
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

        public HttpClientRequester build() {
            return new HttpClientRequester(
                    baseUrl == null ? URI.create("https://discord.com/api/v10") : baseUrl,
                    httpClient == null ? vertx.createHttpClient() : httpClient,
                    tokenSource,
                    rateLimiter);
        }
    }
}
