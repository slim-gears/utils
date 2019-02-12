package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;

public interface ComparableExpression<S, T extends Comparable<T>> extends ObjectExpression<S, T> {
    default BooleanExpression lessThan(T value) {
        return lessThan(ConstantExpression.of(value));
    }

    default BooleanExpression lessThan(ObjectExpression<S, T> value) {
        return BooleanBinaryOperationExpression.create(Type.LessThan, this, value);
    }

    default BooleanExpression greaterThan(T value) {
        return greaterThan(ConstantExpression.of(value));
    }

    default BooleanExpression greaterThan(ObjectExpression<S, T> value) {
        return BooleanBinaryOperationExpression.create(Type.GreaterThan, this, value);
    }

    default BooleanExpression lessOrEq(ObjectExpression<S, T> value) {
        return greaterThan(value).not();
    }

    default BooleanExpression lessOrEq(T value) {
        return greaterThan(value).not();
    }

    default BooleanExpression greaterOrEq(ObjectExpression<S, T> value) {
        return lessThan(value).not();
    }

    default BooleanExpression greaterOrEq(T value) {
        return lessThan(value).not();
    }
}
