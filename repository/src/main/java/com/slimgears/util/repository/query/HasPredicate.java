package com.slimgears.util.repository.query;

import com.slimgears.util.repository.expressions.ObjectExpression;

import javax.annotation.Nullable;

public interface HasPredicate<T> {
    @Nullable ObjectExpression<T, Boolean> predicate();

    interface Builder<_B extends Builder<_B, T>, T> {
        _B predicate(ObjectExpression<T, Boolean> predicate);
    }
}
