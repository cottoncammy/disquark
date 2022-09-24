package org.example.rest.request;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;

// requests flow from here to Requester. subscribed to via subscriber.
// default impl should be aware of distributed architectures (can enqueue a request from somewhere else).
// dont need to serially subscribe if our requester just sneds to snother server
// "process" will extract the request from requestable to pass into the requester.
// we get the bucket of the endpoint via bucketcache
// we associate the bucket with a new or existing (from cache) object <-- TBD
// the object runs the requester (which is rate limited either in the obj or in requester)
//
// determine if the request should be globally rate limited via its endpoint.
//
public class RequestProcessor {

    void complete() {
        // say we have a processor.
        UnicastProcessor<Request> processor = UnicastProcessor.create();

        processor.flatMap(req -> )
    }
}
