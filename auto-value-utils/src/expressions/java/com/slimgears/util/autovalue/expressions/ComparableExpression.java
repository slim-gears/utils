package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;

public interface ComparableExpression<S, T extends Comparable<T>> extends ObjectExpression<S, T> {
    default BooleanExpression<S> lessThan(T value) {
        return lessThan(ConstantExpression.of(value));
    }

    default BooleanExpression<S> lessThan(ObjectExpression<S, T> value) {
        return BooleanBinaryOperationExpression.create(Type.LessThan, this, value);
    }

    default BooleanExpression<S> greaterThan(T value) {
        return greaterThan(ConstantExpression.of(value));
    }

    default BooleanExpression<S> greaterThan(ObjectExpression<S, T> value) {
        return BooleanBinaryOperationExpression.create(Type.GreaterThan, this, value);
    }

    default BooleanExpression<S> lessOrEq(ObjectExpression<S, T> value) {
        return greaterThan(value).not();
    }

    default BooleanExpression<S> lessOrEq(T value) {
        return greaterThan(value).not();
    }

    default BooleanExpression<S> greaterOrEq(ObjectExpression<S, T> value) {
        return lessThan(value).not();
    }

    default BooleanExpression<S> greaterOrEq(T value) {
        return lessThan(value).not();
    }

    default BooleanExpression<S> betweenInclusive(ObjectExpression<S, T> min, ObjectExpression<S, T> max) {
        return lessThan(min).or(greaterThan(max)).not();
    }

    default BooleanExpression<S> betweenExclusive(ObjectExpression<S, T> min, ObjectExpression<S, T> max) {
        return greaterThan(min).and(lessThan(max));
    }
}
