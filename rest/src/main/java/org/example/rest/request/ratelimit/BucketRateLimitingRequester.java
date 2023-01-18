package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

public class BucketRateLimitingRequester implements Requester<HttpResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(BucketRateLimitingRequester.class);
    private final Requester<HttpResponse> requester;
    private final Map<BucketCacheKey, String> bucketCache = new HashMap<>();
    private final Map<String, BucketRateLimitingRequestStream> requestStreamCache = new HashMap<>();

    public BucketRateLimitingRequester(Requester<HttpResponse> requester) {
        this.requester = requester;
    }

    private BucketRateLimitingRequestStream getRequestStream(BucketCacheKey key) {
        String bucket = bucketCache.get(key);
        BucketRateLimitingRequestStream requestStream = requestStreamCache.get(bucket);

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
            if (!bucketCache.containsKey(key)) {
                LOG.debug("Caching bucket value {} for key {}", bucket, key);
                bucketCache.put(key, bucket);
            }

            if (!requestStreamCache.containsKey(bucket)) {
                LOG.debug("Caching request stream for bucket {} (key: {})", bucket, key);
                requestStreamCache.put(bucket, requestStream);
            }
        };

        CompletableRequest completableRequest = new CompletableRequest(request, bucketConsumer);
        requestStream.onNext(completableRequest);

        return completableRequest.getResponsePromise().future()
                .ifNoItem().after(Duration.ofSeconds(5)).failWith(new IllegalStateException(
                        "No item (or failure) event received after 5000ms timeout"));
    }
}
