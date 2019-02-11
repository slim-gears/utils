package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.BinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.NumericExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

@AutoValue
public abstract class NumericBinaryOperationExpression<T1, T2, V extends Number & Comparable<V>> implements BinaryOperationExpression<T1, T2, V>, NumericExpression<V> {
    @JsonCreator
    public static <T1, T2, V extends Number & Comparable<V>> NumericBinaryOperationExpression<T1, T2, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("left") ObjectExpression<T1> left,
            @JsonProperty("right") ObjectExpression<T2> right) {
        return new AutoValue_NumericBinaryOperationExpression<>(type, left, right);
    }
}
