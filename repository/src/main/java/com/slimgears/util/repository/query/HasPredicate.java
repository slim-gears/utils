package com.slimgears.util.repository.query;

import com.slimgears.util.repository.expressions.ObjectExpression;

import javax.annotation.Nullable;

public interface HasPredicate<T> {
    @Nullable ObjectExpression<T, Boolean> predicate();
}
