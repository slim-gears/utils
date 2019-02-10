package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface BooleanBinaryOperationExpression<V1, V2> extends BinaryOperationExpression<V1, V2, Boolean>, BooleanExpression {
    @JsonCreator
    static <V1, V2> BooleanBinaryOperationExpression<V1, V2> create(
            @JsonProperty("type") ExpressionType type,
            @JsonProperty("left") Expression<V1> left,
            @JsonProperty("right") Expression<V2> right) {
        return new BooleanBinaryOperationExpression<V1, V2>() {
            @Override
            public Expression<V1> left() {
                return left;
            }

            @Override
            public Expression<V2> right() {
                return right;
            }

            @Override
            public ExpressionType type() {
                return type;
            }
        };
    }
}
