package org.example.rest.request.ratelimit;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class BucketCacheInserter implements Consumer<String> {
    private final BucketCacheKey key;
    private final Map<BucketCacheKey, String> bucketCache;
    private final AtomicBoolean inserted = new AtomicBoolean();

    public BucketCacheInserter(BucketCacheKey key, Map<BucketCacheKey, String> bucketCache) {
        this.key = key;
        this.bucketCache = bucketCache;
    }

    @Override
    public void accept(String s) {
        if (inserted.compareAndSet(false, true)) {
            bucketCache.put(key, s);
        }
    }
}
