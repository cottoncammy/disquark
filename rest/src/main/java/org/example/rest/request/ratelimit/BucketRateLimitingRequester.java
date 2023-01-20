package org.example.rest.request.ratelimit;

import java.time.Duration;
import java.util.function.Consumer;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import org.example.rest.request.Request;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;
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
                .expireAfterAccess(Duration.ofSeconds(30))
                .evictionListener(((k, v, cause) -> {
                    LOG.debug("Request stream for bucket {} was removed from cache, cause: {}", k, cause);
                }))
                .build();
    }

    private Caffeine<Object, Object> cacheBuilder() {
        return Caffeine.newBuilder().executor(Infrastructure.getDefaultExecutor());
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
        } else {
            LOG.debug("Fetched existing request stream matching bucket {} for key {}", bucket, key);
        }

        if (!requestStream.isSubscribed()) {
            requestStream.subscribe();
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

        Consumer<String> bucketConsumer = bucket -> {
            bucketCache.get(key, k -> {
                LOG.debug("Caching bucket value {} for key {}", bucket, key);
                return bucket;
            });

            requestStreamCache.get(bucket, k -> {
                LOG.debug("Caching request stream for bucket {} (key: {})", bucket, key);
                return requestStream;
            });
        };

        CompletableRequest completableRequest = new CompletableRequest(request, bucketConsumer);
        requestStream.onNext(completableRequest);

        return completableRequest.getResponsePromise().future()
                .ifNoItem().after(Duration.ofSeconds(5)).failWith(new IllegalStateException(
                        "No item (or failure) event received after 5000ms timeout"));
    }
}
