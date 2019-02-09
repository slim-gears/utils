package com.slimgears.util.autovalue.expressions;

public interface NumericUnaryOperationExpression<V extends Number & Comparable<V>> extends UnaryOperationExpression<V, V>, NumericValueExpression<V> {
    static <V extends Number & Comparable<V>> NumericUnaryOperationExpression<V> create(String type, ValueExpression<V> value) {
        return new NumericUnaryOperationExpression<V>() {
            @Override
            public ValueExpression<V> operand() {
                return value;
            }

            @Override
            public String type() {
                return type;
            }
        };
    }
}
