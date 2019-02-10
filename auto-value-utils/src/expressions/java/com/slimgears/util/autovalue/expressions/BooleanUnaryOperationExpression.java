package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface BooleanUnaryOperationExpression<V> extends UnaryOperationExpression<V, Boolean>, BooleanExpression {
    @JsonCreator
    static <V> BooleanUnaryOperationExpression<V> create(
            @JsonProperty("type") ExpressionType type,
            @JsonProperty("operand") Expression<V> operand) {
        return new BooleanUnaryOperationExpression<V>() {
            @Override
            public Expression<V> operand() {
                return operand;
            }

            @Override
            public ExpressionType type() {
                return type;
            }
        };
    }
}
