package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.NumericExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.UnaryOperationExpression;

@AutoValue
public abstract class NumericUnaryOperationExpression<S, T, V extends Number & Comparable<V>> implements UnaryOperationExpression<S, T, V>, NumericExpression<S, V> {
    @JsonCreator
    public static <S, T, V extends Number & Comparable<V>> NumericUnaryOperationExpression<S, T, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("operand") ObjectExpression<S, T> operand) {
        return new AutoValue_NumericUnaryOperationExpression<>(type, operand);
    }
}
