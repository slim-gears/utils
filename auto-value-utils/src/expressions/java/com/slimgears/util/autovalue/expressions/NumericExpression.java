package com.slimgears.util.autovalue.expressions;

public interface NumericExpression<V extends Number & Comparable<V>> extends ComparableExpression<V> {
    default NumericExpression<V> add(NumericExpression<V> value) {
        return NumericBinaryOperationExpression.create(ExpressionType.Add, this, value);
    }

    default NumericExpression<V> add(V value) {
        return add(ConstantExpression.of(value));
    }

    default NumericExpression<V> negate() {
        return NumericUnaryOperationExpression.create(ExpressionType.Negate, this);
    }

    default NumericExpression<V> sub(NumericExpression<V> value) {
        return NumericBinaryOperationExpression.create(ExpressionType.Sub, this, value);
    }

    default NumericExpression<V> sub(V value) {
        return sub(ConstantExpression.of(value));
    }

    default NumericExpression<V> mul(NumericExpression<V> value) {
        return NumericBinaryOperationExpression.create(ExpressionType.Mul, this, value);
    }

    default NumericExpression<V> mul(V value) {
        return mul(ConstantExpression.of(value));
    }

    default NumericExpression<V> div(NumericExpression<V> value) {
        return NumericBinaryOperationExpression.create(ExpressionType.Div, this, value);
    }

    default NumericExpression<V> div(V value) {
        return div(ConstantExpression.of(value));
    }
}
