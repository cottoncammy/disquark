package io.disquark.rest.request.ratelimit;

import java.time.Duration;
import java.util.function.Consumer;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requester;
import io.disquark.rest.response.HttpResponse;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketRateLimitingRequester implements Requester<HttpResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(BucketRateLimitingRequester.class);
    private final Requester<HttpResponse> requester;
    private final Cache<BucketCacheKey, String> bucketCache;
    private final Cache<String, BucketRateLimitingRequestStream> requestStreamCache;

    public BucketRateLimitingRequester(Requester<HttpResponse> requester) {
        this.requester = requester;
        this.bucketCache = cacheBuilder()
                .maximumSize(10_000)
                .evictionListener(((k, v, cause) -> {
                    LOG.debug("Bucket cache entry {} - {} was removed, cause: {}", k, v, cause);
                }))
                .build();
        this.requestStreamCache = cacheBuilder()
                .expireAfterAccess(Duration.ofSeconds(20))
                .evictionListener(((k, v, cause) -> {
                    LOG.debug("Request stream for bucket {} was removed from cache, cause: {}", k, cause);
                }))
                .build();
    }

    private Caffeine<Object, Object> cacheBuilder() {
        return Caffeine.newBuilder().executor(Infrastructure.getDefaultWorkerPool());
    }

    private BucketRateLimitingRequestStream getRequestStream(BucketCacheKey key) {
        String bucket = bucketCache.getIfPresent(key);
        BucketRateLimitingRequestStream requestStream = null;
        if (bucket != null) {
            requestStream = requestStreamCache.getIfPresent(bucket);
        }

        if (requestStream == null) {
            LOG.debug("Creating new request stream for bucket key {}", key);
            requestStream = new BucketRateLimitingRequestStream(key, requester);
            requestStream.subscribe();
        } else {
            LOG.debug("Fetched existing request stream matching bucket {} for key {}", bucket, key);
        }

        return requestStream;
    }

    public Requester<HttpResponse> getRequester() {
        return requester;
    }

    @Override
    public Uni<HttpResponse> request(Request request) {
        BucketCacheKey key = BucketCacheKey.create(request);
        BucketRateLimitingRequestStream requestStream = getRequestStream(key);

        Consumer<String> bucketCallback = bucket -> {
            bucketCache.get(key, k -> {
                LOG.debug("Caching bucket value {} for key {}", bucket, key);
                return bucket;
            });

            requestStreamCache.get(bucket, k -> {
                LOG.debug("Caching request stream for bucket {} (key: {})", bucket, key);
                return requestStream;
            });
        };

        CompletableRequest completableRequest = new CompletableRequest(request, bucketCallback);
        requestStream.onNext(completableRequest);

        return completableRequest.getResponsePromise().future()
                .ifNoItem().after(Duration.ofSeconds(5)).failWith(new IllegalStateException(
                        "No item (or failure) event received after 5000ms timeout"));
    }
}
