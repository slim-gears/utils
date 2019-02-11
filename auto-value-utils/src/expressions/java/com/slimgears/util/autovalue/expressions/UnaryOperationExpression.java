package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UnaryOperationExpression<T, R> extends ObjectExpression<R> {
    @JsonProperty ObjectExpression<T> operand();
}
