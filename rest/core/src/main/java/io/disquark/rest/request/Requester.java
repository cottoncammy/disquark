package io.disquark.rest.request;

import io.disquark.rest.response.Response;
import io.smallrye.mutiny.Uni;

@FunctionalInterface
public interface Requester<T extends Response> {

    Uni<T> request(Request request);
}
