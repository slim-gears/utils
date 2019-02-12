package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.NumericBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericUnaryOperationExpression;

public interface NumericExpression<S, T extends Number & Comparable<T>> extends ComparableExpression<S, T> {
    default NumericExpression<S, T> add(NumericExpression<S, T> value) {
        return NumericBinaryOperationExpression.create(Type.Add, this, value);
    }

    default NumericExpression<S, T> add(T value) {
        return add(ConstantExpression.of(value));
    }

    default NumericExpression<S, T> negate() {
        return NumericUnaryOperationExpression.create(Type.Negate, this);
    }

    default NumericExpression<S, T> sub(NumericExpression<S, T> value) {
        return NumericBinaryOperationExpression.create(Type.Sub, this, value);
    }

    default NumericExpression<S, T> sub(T value) {
        return sub(ConstantExpression.of(value));
    }

    default NumericExpression<S, T> mul(NumericExpression<S, T> value) {
        return NumericBinaryOperationExpression.create(Type.Mul, this, value);
    }

    default NumericExpression<S, T> mul(T value) {
        return mul(ConstantExpression.of(value));
    }

    default NumericExpression<S, T> div(NumericExpression<S, T> value) {
        return NumericBinaryOperationExpression.create(Type.Div, this, value);
    }

    default NumericExpression<S, T> div(T value) {
        return div(ConstantExpression.of(value));
    }
}
