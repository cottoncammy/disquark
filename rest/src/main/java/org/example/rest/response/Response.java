package org.example.rest.response;

import io.smallrye.mutiny.Uni;

public interface Response {

    <T> Uni<T> as(Class<T> type);
}
