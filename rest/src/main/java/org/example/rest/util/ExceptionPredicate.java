package org.example.rest.util;

import java.util.function.Predicate;

public interface ExceptionPredicate extends Predicate<Throwable> {

    static Predicate<Throwable> is(Class<? extends Throwable> type) {
        return t -> t.getClass().equals(type);
    }

    static Predicate<Throwable> wasCausedBy(Class<? extends Throwable> type) {
        return t -> is(type).test(t.getCause());
    }
}
