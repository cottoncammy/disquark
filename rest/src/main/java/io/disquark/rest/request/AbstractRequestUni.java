package io.disquark.rest.request;

import io.smallrye.mutiny.operators.AbstractUni;

public abstract class AbstractRequestUni<T> extends AbstractUni<T> implements Requestable {

    public abstract Requester<?> requester();
}
