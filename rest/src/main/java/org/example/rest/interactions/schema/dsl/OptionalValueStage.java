package org.example.rest.interactions.schema.dsl;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OptionalValueStage<S, T> {
    private final S previousStage;
    private final BiConsumer<S, T> isEqualMutator;
    private final Consumer<S> isPresentMutator;
    private final Consumer<S> isEmptyMutator;

    protected OptionalValueStage(
            S previousStage,
            BiConsumer<S, T> isEqualMutator,
            Consumer<S> isPresentMutator,
            Consumer<S> isEmptyMutator) {
        this.previousStage = previousStage;
        this.isEqualMutator = isEqualMutator;
        this.isPresentMutator = isPresentMutator;
        this.isEmptyMutator = isEmptyMutator;
    }

    public S is(T t) {
        isEqualMutator.accept(previousStage, t);
        return previousStage;
    }

    public S isPresent() {
        isPresentMutator.accept(previousStage);
        return previousStage;
    }

    public S isEmpty() {
        isEmptyMutator.accept(previousStage);
        return previousStage;
    }
}
