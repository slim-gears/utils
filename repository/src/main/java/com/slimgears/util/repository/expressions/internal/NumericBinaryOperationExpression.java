package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.BinaryOperationExpression;
import com.slimgears.util.repository.expressions.NumericExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;

@AutoValue
public abstract class NumericBinaryOperationExpression<S, T1, T2, V extends Number & Comparable<V>> implements BinaryOperationExpression<S, T1, T2, V>, NumericExpression<S, V> {
    @JsonCreator
    public static <S, T1, T2, V extends Number & Comparable<V>> NumericBinaryOperationExpression<S, T1, T2, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("left") ObjectExpression<S, T1> left,
            @JsonProperty("right") ObjectExpression<S, T2> right) {
        return new AutoValue_NumericBinaryOperationExpression<>(type, left, right);
    }
}
