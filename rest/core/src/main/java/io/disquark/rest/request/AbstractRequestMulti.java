package io.disquark.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.smallrye.mutiny.operators.AbstractMulti;

public abstract class AbstractRequestMulti<T> extends AbstractMulti<T> implements Requestable {

    @JsonIgnore
    public abstract Requester<?> requester();
}
