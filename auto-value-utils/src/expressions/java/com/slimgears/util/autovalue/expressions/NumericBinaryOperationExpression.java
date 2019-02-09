package com.slimgears.util.autovalue.expressions;

public interface NumericBinaryOperationExpression<V extends Number & Comparable<V>> extends BinaryOperationExpression<V, V, V>, NumericValueExpression<V> {
    static <V extends Number & Comparable<V>> NumericValueExpression<V> create(String type, ValueExpression<V> left, ValueExpression<V> right) {
        return new NumericBinaryOperationExpression<V>() {
            @Override
            public ValueExpression<V> left() {
                return left;
            }

            @Override
            public ValueExpression<V> right() {
                return right;
            }

            @Override
            public String type() {
                return type;
            }
        };
    }
}
