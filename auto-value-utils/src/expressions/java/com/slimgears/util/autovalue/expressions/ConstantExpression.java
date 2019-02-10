package com.slimgears.util.autovalue.expressions;

public interface ConstantExpression<V> extends Expression<V> {
    V value();

    static <V> ConstantExpression<V> of(V value) {
        return new ConstantExpression<V>() {
            @Override
            public V value() {
                return value;
            }

            @Override
            public ExpressionType type() {
                return ExpressionType.Constant;
            }
        };
    }

    static <V extends Number & Comparable<V>> NumericConstantExpression<V> of(V value) {
        return NumericConstantExpression.of(value);
    }

    static StringConstantExpression of(String value) {
        return StringConstantExpression.of(value);
    }
}
