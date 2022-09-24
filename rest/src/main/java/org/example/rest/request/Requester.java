package org.example.rest.request;

import io.smallrye.mutiny.Uni;
import org.example.rest.response.Response;

@FunctionalInterface
public interface Requester {

    Uni<Response> request(Requestable requestable);
}
