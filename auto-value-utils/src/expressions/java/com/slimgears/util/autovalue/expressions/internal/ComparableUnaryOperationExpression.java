package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.ComparableExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.UnaryOperationExpression;

@AutoValue
public abstract class ComparableUnaryOperationExpression<S, T, V extends Comparable<V>> implements UnaryOperationExpression<S, T, V>, ComparableExpression<S, V> {
    @JsonCreator
    public static <S, T, V extends Comparable<V>> ComparableUnaryOperationExpression<S, T, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("operand") ObjectExpression<S, T> operand) {
        return new AutoValue_ComparableUnaryOperationExpression<>(type, operand);
    }
}
