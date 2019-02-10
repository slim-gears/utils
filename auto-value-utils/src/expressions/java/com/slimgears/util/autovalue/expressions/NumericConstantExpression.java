package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface NumericConstantExpression<V extends Number & Comparable<V>> extends ConstantExpression<V>, NumericExpression<V> {
    @JsonCreator
    static <V extends Number & Comparable<V>> NumericConstantExpression<V> of(@JsonProperty("value") V value) {
        return new NumericConstantExpression<V>() {
            @Override
            public V value() {
                return value;
            }

            @Override
            public ExpressionType type() {
                return ExpressionType.NumericConstant;
            }
        };
    }
}
