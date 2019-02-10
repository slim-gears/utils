package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface NumericBinaryOperationExpression<V extends Number & Comparable<V>> extends BinaryOperationExpression<V, V, V>, NumericExpression<V> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    static <V extends Number & Comparable<V>> NumericExpression<V> create(
            @JsonProperty ExpressionType type,
            @JsonProperty Expression<V> left,
            @JsonProperty Expression<V> right) {
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
