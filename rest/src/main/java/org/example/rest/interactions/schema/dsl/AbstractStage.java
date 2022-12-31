package org.example.rest.interactions.schema.dsl;

public abstract class AbstractStage<T> {
    protected final T previousStage;

    protected AbstractStage(T previousStage) {
        this.previousStage = previousStage;
    }
}
