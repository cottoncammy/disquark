package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;
import org.example.rest.response.Response;

import java.util.Map;

// TODO how do we make this class distributed-aware
public class BucketRateLimitingRequester implements Requester {
    private final Requester requester;
    private final Map<BucketCacheKey, String> bucketCache;
    private final Map<String, BucketRateLimitingRequestStream> requestStreamCache;

    private BucketRateLimitingRequester(
            Requester requester,
            Map<BucketCacheKey, String> bucketCache,
            Map<String, BucketRateLimitingRequestStream> requestStreamCache) {
        this.requester = requester;
        this.bucketCache = bucketCache;
        this.requestStreamCache = requestStreamCache;
    }

    @Override
    public Uni<Response> request(Request request) {
        BucketCacheKey key = BucketCacheKey.create(request);
        String bucket = bucketCache.get(key);

        BucketRateLimitingRequestStream requestStream;
        if (bucket != null) {
            // TODO check if this is null
            requestStream = requestStreamCache.get(bucket);
        } else {
            requestStream = new BucketRateLimitingRequestStream(requester, new BucketCacheInserter(key, bucketCache));
            requestStream.subscribe();
        }

        requestStream.onNext(request);
        return request.responsePromise().future();
    }
}
