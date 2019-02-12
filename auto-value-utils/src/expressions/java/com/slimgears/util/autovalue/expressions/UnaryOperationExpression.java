package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UnaryOperationExpression<S, T, R> extends ObjectExpression<S, R> {
    @JsonProperty ObjectExpression<S, T> operand();
}
