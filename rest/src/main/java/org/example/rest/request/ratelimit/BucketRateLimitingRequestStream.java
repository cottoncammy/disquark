package org.example.rest.request.ratelimit;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.vertx.mutiny.core.Promise;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;

import java.time.Duration;

class BucketRateLimitingRequestStream {
    private final Requester<HttpResponse> requester;
    private final Promise<String> bucketPromise = Promise.promise();
    private final BroadcastProcessor<CompletableRequest> processor = BroadcastProcessor.create();

    private volatile boolean subscribed;

    public BucketRateLimitingRequestStream(Requester<HttpResponse> requester) {
        this.requester = requester;
    }

    public void onNext(CompletableRequest request) {
        processor.onNext(request);
    }

    public void subscribe() {
        Uni.createFrom().voidItem().invoke(() -> subscribed = true)
                .onItem().transformToMulti(x -> processor)
                .ifNoItem().after(Duration.ofSeconds(30)).recoverWithCompletion()
                .onCompletion().invoke(() -> subscribed = false)
                .subscribe().withSubscriber(new BucketRateLimitingRequestSubscriber(requester, bucketPromise));
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public Uni<String> getBucket() {
        return bucketPromise.future();
    }
}
