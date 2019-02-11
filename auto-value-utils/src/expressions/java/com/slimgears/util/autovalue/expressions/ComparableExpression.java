package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;

public interface ComparableExpression<T extends Comparable<T>> extends ObjectExpression<T> {
    default BooleanExpression lessThan(T value) {
        return lessThan(ConstantExpression.of(value));
    }

    default BooleanExpression lessThan(ObjectExpression<T> value) {
        return BooleanBinaryOperationExpression.create(Type.LessThan, this, value);
    }

    default BooleanExpression greaterThan(T value) {
        return greaterThan(ConstantExpression.of(value));
    }

    default BooleanExpression greaterThan(ObjectExpression<T> value) {
        return BooleanBinaryOperationExpression.create(Type.GreaterThan, this, value);
    }

    default BooleanExpression lessOrEq(ObjectExpression<T> value) {
        return greaterThan(value).not();
    }

    default BooleanExpression lessOrEq(T value) {
        return greaterThan(value).not();
    }

    default BooleanExpression greaterOrEq(ObjectExpression<T> value) {
        return lessThan(value).not();
    }

    default BooleanExpression greaterOrEq(T value) {
        return lessThan(value).not();
    }
}
