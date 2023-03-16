package io.disquark.rest.request;

import io.smallrye.mutiny.operators.AbstractMulti;

public abstract class AbstractRequestMulti<T> extends AbstractMulti<T> implements Requestable {

    public abstract Requester<?> requester();
}
