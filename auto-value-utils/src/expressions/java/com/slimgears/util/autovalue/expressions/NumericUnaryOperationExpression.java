package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface NumericUnaryOperationExpression<T, V extends Number & Comparable<V>> extends UnaryOperationExpression<T, V>, NumericExpression<V> {
    @JsonCreator
    static <T, V extends Number & Comparable<V>> NumericUnaryOperationExpression<T, V> create(
            @JsonProperty("type") ExpressionType type,
            @JsonProperty("operand") Expression<T> operand) {
        return new NumericUnaryOperationExpression<T, V>() {
            @Override
            public Expression<T> operand() {
                return operand;
            }

            @Override
            public ExpressionType type() {
                return type;
            }
        };
    }
}
