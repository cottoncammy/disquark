package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;
import org.example.rest.response.Response;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class BucketRateLimitingRequester implements Requester {
    private final Requester requester;
    private final Map<BucketCacheKey, String> bucketCache = new HashMap<>();
    private final Map<String, BucketRateLimitingRequestStream> requestStreamCache = new HashMap<>();

    public static BucketRateLimitingRequester create(Requester requester) {
        return new BucketRateLimitingRequester(requireNonNull(requester));
    }

    private BucketRateLimitingRequester(Requester requester) {
        this.requester = requester;
    }

    private BucketRateLimitingRequestStream getRequestStream(BucketCacheKey key) {
        BucketRateLimitingRequestStream requestStream = requestStreamCache.get(bucketCache.get(key));
        if (requestStream == null) {
            requestStream = new BucketRateLimitingRequestStream(requester);
        }

        if (!requestStream.isSubscribed()) {
            requestStream.subscribe();
        }

        return requestStream;
    }

    @Override
    public Uni<Response> request(Request request) {
        BucketCacheKey key = BucketCacheKey.create(request);
        BucketRateLimitingRequestStream requestStream = getRequestStream(key);
        requestStream.onNext(request);

        return requestStream.getBucket()
                .onItem().invoke(bucket -> {
                    if (!bucketCache.containsKey(key)) {
                        bucketCache.put(key, bucket);
                    }

                    if (!requestStreamCache.containsKey(bucket)) {
                        requestStreamCache.put(bucket, requestStream);
                    }
                })
                .replaceWith(request.responsePromise().future());
    }
}
