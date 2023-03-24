package io.disquark.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.smallrye.mutiny.operators.AbstractUni;

public abstract class AbstractRequestUni<T> extends AbstractUni<T> implements Requestable {

    @JsonIgnore
    public abstract Requester<?> requester();
}
