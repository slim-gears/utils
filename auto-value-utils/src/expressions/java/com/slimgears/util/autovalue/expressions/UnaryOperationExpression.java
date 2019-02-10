package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UnaryOperationExpression<V, R> extends Expression<R> {
    @JsonProperty
    Expression<V> operand();
}
