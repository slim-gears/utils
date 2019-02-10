package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface NumericBinaryOperationExpression<V extends Number & Comparable<V>> extends BinaryOperationExpression<V, V, V>, NumericExpression<V> {
    @JsonCreator
    static <V extends Number & Comparable<V>> NumericBinaryOperationExpression<V> create(
            @JsonProperty("type") ExpressionType type,
            @JsonProperty("left") Expression<V> left,
            @JsonProperty("right") Expression<V> right) {
        return new NumericBinaryOperationExpression<V>() {
            @Override
            public Expression<V> left() {
                return left;
            }

            @Override
            public Expression<V> right() {
                return right;
            }

            @Override
            public ExpressionType type() {
                return type;
            }
        };
    }
}
