package com.slimgears.util.repository.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UnaryOperationExpression<S, T, R> extends ObjectExpression<S, R> {
    @JsonProperty ObjectExpression<S, T> operand();
}
