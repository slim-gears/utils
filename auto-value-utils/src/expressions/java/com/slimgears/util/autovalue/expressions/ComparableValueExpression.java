package com.slimgears.util.autovalue.expressions;

public interface ComparableValueExpression<V extends Comparable<V>> extends ValueExpression<V> {
    default BooleanExpression lessThan(V value) {
        return lessThan(ConstantExpression.of(value));
    }

    default BooleanExpression lessThan(ValueExpression<V> value) {
        return BooleanBinaryOperationExpression.create("lessThan", this, value);
    }

    default BooleanExpression greaterThan(V value) {
        return greaterThan(ConstantExpression.of(value));
    }

    default BooleanExpression greaterThan(ValueExpression<V> value) {
        return BooleanBinaryOperationExpression.create("greaterThan", this, value);
    }

    default BooleanExpression lessOrEq(ValueExpression<V> value) {
        return greaterThan(value).not();
    }

    default BooleanExpression lessOrEq(V value) {
        return greaterThan(value).not();
    }

    default BooleanExpression greaterOrEq(ValueExpression<V> value) {
        return lessThan(value).not();
    }

    default BooleanExpression greaterOrEq(V value) {
        return lessThan(value).not();
    }
}
