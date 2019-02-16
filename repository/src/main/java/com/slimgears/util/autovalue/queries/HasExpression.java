package com.slimgears.util.autovalue.queries;

import com.slimgears.util.autovalue.expressions.ObjectExpression;

import java.util.Collection;

public interface HasExpression<S, T> {
    ObjectExpression<Collection<S>, Collection<T>> expression();

    interface Builder<_B extends Builder<_B, S, T>, S, T> {
        _B expression(ObjectExpression<Collection<S>, Collection<T>> expression);
    }
}
