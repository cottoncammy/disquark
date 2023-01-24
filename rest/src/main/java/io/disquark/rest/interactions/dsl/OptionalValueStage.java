package io.disquark.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

class OptionalValueStage<S, T> {
    private final S previousStage;
    private final BiConsumer<S, T> isEqualSideEffect;
    private final Consumer<S> isPresentSideEffect;
    private final Consumer<S> isEmptySideEffect;

    protected OptionalValueStage(
            S previousStage,
            BiConsumer<S, T> isEqualSideEffect,
            Consumer<S> isPresentSideEffect,
            Consumer<S> isEmptySideEffect) {
        this.previousStage = previousStage;
        this.isEqualSideEffect = isEqualSideEffect;
        this.isPresentSideEffect = isPresentSideEffect;
        this.isEmptySideEffect = isEmptySideEffect;
    }

    public S is(T value) {
        isEqualSideEffect.accept(previousStage, requireNonNull(value, "value"));
        return previousStage;
    }

    public S isPresent() {
        isPresentSideEffect.accept(previousStage);
        return previousStage;
    }

    public S isEmpty() {
        isEmptySideEffect.accept(previousStage);
        return previousStage;
    }
}
