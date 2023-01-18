package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

class BucketRateLimitingRequestStream {
    private static final Logger LOG = LoggerFactory.getLogger(BucketRateLimitingRequestStream.class);
    private final BucketCacheKey bucketKey;
    private final Requester<HttpResponse> requester;
    private final UnicastProcessor<CompletableRequest> processor = UnicastProcessor.create();

    private volatile boolean subscribed;

    public BucketRateLimitingRequestStream(BucketCacheKey bucketKey, Requester<HttpResponse> requester) {
        this.bucketKey = bucketKey;
        this.requester = requester;
    }

    public void onNext(CompletableRequest request) {
        processor.onNext(request);
    }

    public void subscribe() {
        LOG.debug("Subscribing to request stream for buckets matching key {}", bucketKey);
        Uni.createFrom().voidItem()
                .invoke(() -> subscribed = true)
                .onItem().transformToMulti(x -> processor)
                .ifNoItem().after(Duration.ofSeconds(30)).recoverWithCompletion()
                .onCompletion().invoke(() -> {
                    LOG.debug("Unsubscribing from stream: no requests received for buckets matching key {} after timeout", bucketKey);
                    subscribed = false;
                })
                .subscribe().withSubscriber(new BucketRateLimitingRequestSubscriber(bucketKey, requester));
    }

    public boolean isSubscribed() {
        return subscribed;
    }
}
