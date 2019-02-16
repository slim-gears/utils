package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.BinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

@AutoValue
public abstract class ObjectBinaryOperationExpression<S, T1, T2, V> implements BinaryOperationExpression<S, T1, T2, V>, ObjectExpression<S, V> {
    @JsonCreator
    public static <S, T1, T2, V extends Comparable<V>> ObjectBinaryOperationExpression<S, T1, T2, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("left") ObjectExpression<S, T1> left,
            @JsonProperty("right") ObjectExpression<S, T2> right) {
        return new AutoValue_ObjectBinaryOperationExpression<>(type, left, right);
    }
}
