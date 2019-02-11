package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.NumericBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericUnaryOperationExpression;

public interface NumericExpression<T extends Number & Comparable<T>> extends ComparableExpression<T> {
    default NumericExpression<T> add(NumericExpression<T> value) {
        return NumericBinaryOperationExpression.create(Type.Add, this, value);
    }

    default NumericExpression<T> add(T value) {
        return add(ConstantExpression.of(value));
    }

    default NumericExpression<T> negate() {
        return NumericUnaryOperationExpression.create(Type.Negate, this);
    }

    default NumericExpression<T> sub(NumericExpression<T> value) {
        return NumericBinaryOperationExpression.create(Type.Sub, this, value);
    }

    default NumericExpression<T> sub(T value) {
        return sub(ConstantExpression.of(value));
    }

    default NumericExpression<T> mul(NumericExpression<T> value) {
        return NumericBinaryOperationExpression.create(Type.Mul, this, value);
    }

    default NumericExpression<T> mul(T value) {
        return mul(ConstantExpression.of(value));
    }

    default NumericExpression<T> div(NumericExpression<T> value) {
        return NumericBinaryOperationExpression.create(Type.Div, this, value);
    }

    default NumericExpression<T> div(T value) {
        return div(ConstantExpression.of(value));
    }
}
