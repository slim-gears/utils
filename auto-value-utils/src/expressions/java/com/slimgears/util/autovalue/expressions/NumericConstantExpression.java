package com.slimgears.util.autovalue.expressions;

public interface NumericConstantExpression<V extends Number & Comparable<V>> extends ConstantExpression<V>, NumericValueExpression<V> {
    static <V extends Number & Comparable<V>> NumericConstantExpression<V> of(V value) {
        return new NumericConstantExpression<V>() {
            @Override
            public V value() {
                return value;
            }

            @Override
            public String type() {
                return "numericConstant";
            }
        };
    }
}
