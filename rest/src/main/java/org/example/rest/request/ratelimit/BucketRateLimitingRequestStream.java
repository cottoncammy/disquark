package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;
import org.jboss.logging.Logger;

import java.time.Duration;

class BucketRateLimitingRequestStream {
    private static final Logger LOG = Logger.getLogger(BucketRateLimitingRequestStream.class);
    private final BucketCacheKey bucketKey;
    private final Requester<HttpResponse> requester;
    private final Promise<String> bucketPromise = Promise.promise();
    private final BroadcastProcessor<CompletableRequest> processor = BroadcastProcessor.create();

    private volatile boolean subscribed;

    public BucketRateLimitingRequestStream(BucketCacheKey  bucketKey, Requester<HttpResponse> requester) {
        this.bucketKey = bucketKey;
        this.requester = requester;
    }

    public void onNext(CompletableRequest request) {
        processor.onNext(request);
    }

    public void subscribe() {
        LOG.debugf("Subscribing to request stream for buckets matching key %s", bucketKey);
        Uni.createFrom().voidItem()
                .invoke(() -> subscribed = true)
                .onItem().transformToMulti(x -> processor)
                .ifNoItem().after(Duration.ofSeconds(30)).recoverWithCompletion()
                .onCompletion().invoke(() -> {
                    LOG.debugf("Unsubscribing from stream: no requests received for buckets matching key %s after timeout", bucketKey);
                    subscribed = false;
                })
                .subscribe().withSubscriber(new BucketRateLimitingRequestSubscriber(bucketKey, requester, bucketPromise));
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public Uni<String> getBucket() {
        return bucketPromise.future();
    }
}
