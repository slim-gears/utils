package com.slimgears.util.autovalue.expressions;

public interface ComparableExpression<V extends Comparable<V>> extends Expression<V> {
    default BooleanExpression lessThan(V value) {
        return lessThan(ConstantExpression.of(value));
    }

    default BooleanExpression lessThan(Expression<V> value) {
        return BooleanBinaryOperationExpression.create(ExpressionType.LessThan, this, value);
    }

    default BooleanExpression greaterThan(V value) {
        return greaterThan(ConstantExpression.of(value));
    }

    default BooleanExpression greaterThan(Expression<V> value) {
        return BooleanBinaryOperationExpression.create(ExpressionType.GreaterThan, this, value);
    }

    default BooleanExpression lessOrEq(Expression<V> value) {
        return greaterThan(value).not();
    }

    default BooleanExpression lessOrEq(V value) {
        return greaterThan(value).not();
    }

    default BooleanExpression greaterOrEq(Expression<V> value) {
        return lessThan(value).not();
    }

    default BooleanExpression greaterOrEq(V value) {
        return lessThan(value).not();
    }
}
