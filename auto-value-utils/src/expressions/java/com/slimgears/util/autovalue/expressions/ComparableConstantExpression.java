package com.slimgears.util.autovalue.expressions;

public interface ComparableConstantExpression<V extends Comparable<V>> extends ComparableValueExpression<V> {
    V value();

    static <V extends Comparable<V>> ComparableConstantExpression<V> of(V value) {
        return new ComparableConstantExpression<V>() {
            @Override
            public V value() {
                return value;
            }

            @Override
            public String type() {
                return "comparableConstant";
            }
        };
    }
}
