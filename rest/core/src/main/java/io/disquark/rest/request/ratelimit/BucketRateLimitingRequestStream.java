package io.disquark.rest.request.ratelimit;

import java.time.Duration;

import io.disquark.rest.request.Requester;
import io.disquark.rest.response.HttpResponse;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BucketRateLimitingRequestStream {
    private static final Logger LOG = LoggerFactory.getLogger(BucketRateLimitingRequestStream.class);
    private final BucketCacheKey bucketKey;
    private final Requester<HttpResponse> requester;
    private final UnicastProcessor<CompletableRequest> processor = UnicastProcessor.create();

    public BucketRateLimitingRequestStream(BucketCacheKey bucketKey, Requester<HttpResponse> requester) {
        this.bucketKey = bucketKey;
        this.requester = requester;
    }

    public void onNext(CompletableRequest request) {
        processor.onNext(request);
    }

    public void subscribe() {
        Uni.createFrom().voidItem()
                .invoke(() -> LOG.debug("Subscribing to request stream for buckets matching key {}", bucketKey))
                .onItem().transformToMulti(x -> processor)
                .ifNoItem().after(Duration.ofSeconds(40)).recoverWithCompletion()
                .onCompletion()
                .invoke(() -> LOG.debug(
                        "Unsubscribing from stream: no requests received for buckets matching key {} after timeout", bucketKey))
                .subscribe().withSubscriber(new BucketRateLimitingRequestSubscriber(bucketKey, requester));
    }
}
