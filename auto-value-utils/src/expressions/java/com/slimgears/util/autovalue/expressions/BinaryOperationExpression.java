package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface BinaryOperationExpression<T1, T2, R> extends ObjectExpression<R> {
    @JsonProperty ObjectExpression<T1> left();
    @JsonProperty ObjectExpression<T2> right();
}
