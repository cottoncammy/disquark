package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import org.example.rest.request.Request;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

class BucketRateLimitingRequester implements Requester<HttpResponse> {
    private final Requester<HttpResponse> requester;
    private final Map<BucketCacheKey, String> bucketCache = new HashMap<>();
    private final Map<String, BucketRateLimitingRequestStream> requestStreamCache = new HashMap<>();

    public BucketRateLimitingRequester(Requester<HttpResponse> requester) {
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
    public Uni<HttpResponse> request(Request request) {
        BucketCacheKey key = BucketCacheKey.create(request);
        BucketRateLimitingRequestStream requestStream = getRequestStream(key);

        CompletableRequest completableRequest = new CompletableRequest(request);
        requestStream.onNext(completableRequest);

        return requestStream.getBucket()
                .onItem().invoke(bucket -> {
                    if (!bucketCache.containsKey(key)) {
                        bucketCache.put(key, bucket);
                    }

                    if (!requestStreamCache.containsKey(bucket)) {
                        requestStreamCache.put(bucket, requestStream);
                    }
                })
                .replaceWith(completableRequest.getPromise().future());
    }
}
