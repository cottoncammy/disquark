package io.disquark.rest.util;

import java.util.Arrays;
import java.util.function.Predicate;

public interface ExceptionPredicate extends Predicate<Throwable> {

    @SafeVarargs
    static Predicate<Throwable> is(Class<? extends Throwable>... type) {
        return t -> Arrays.asList(type).contains(t.getClass());
    }

    @SafeVarargs
    static Predicate<Throwable> isNot(Class<? extends Throwable>... type) {
        return Predicate.not(is(type));
    }

    static Predicate<Throwable> wasCausedBy(Class<? extends Throwable> type) {
        return t -> is(type).test(t.getCause());
    }
}
