package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface BinaryOperationExpression<S, T1, T2, R> extends ObjectExpression<S, R> {
    @JsonProperty ObjectExpression<S, T1> left();
    @JsonProperty ObjectExpression<S, T2> right();
}
