package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface BinaryOperationExpression<T1, T2, R> extends Expression<R> {
    @JsonProperty
    Expression<T1> left();
    @JsonProperty
    Expression<T2> right();
}
