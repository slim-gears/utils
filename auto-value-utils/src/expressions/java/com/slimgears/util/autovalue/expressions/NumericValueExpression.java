package com.slimgears.util.autovalue.expressions;

public interface NumericValueExpression<V extends Number & Comparable<V>> extends ComparableValueExpression<V> {
    default NumericValueExpression<V> add(NumericValueExpression<V> value) {
        return NumericBinaryOperationExpression.create("add", this, value);
    }

    default NumericValueExpression<V> add(V value) {
        return add(ConstantExpression.of(value));
    }

    default NumericValueExpression<V> negate() {
        return NumericUnaryOperationExpression.create("negate", this);
    }

    default NumericValueExpression<V> sub(NumericValueExpression<V> value) {
        return add(value.negate());
    }

    default NumericValueExpression<V> sub(V value) {
        return sub(ConstantExpression.of(value));
    }

    default NumericValueExpression<V> mul(NumericValueExpression<V> value) {
        return NumericBinaryOperationExpression.create("mul", this, value);
    }

    default NumericValueExpression<V> mul(V value) {
        return mul(ConstantExpression.of(value));
    }

    default NumericValueExpression<V> div(NumericValueExpression<V> value) {
        return NumericBinaryOperationExpression.create("div", this, value);
    }

    default NumericValueExpression<V> div(V value) {
        return div(ConstantExpression.of(value));
    }
}
