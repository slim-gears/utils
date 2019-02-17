package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.BooleanExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.UnaryOperationExpression;

@AutoValue
public abstract class BooleanUnaryOperationExpression<S, V> implements UnaryOperationExpression<S, V, Boolean>, BooleanExpression<S> {
    @JsonCreator
    public static <S, V> BooleanUnaryOperationExpression<S, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("operand") ObjectExpression<S, V> operand) {
        return new AutoValue_BooleanUnaryOperationExpression<>(type, operand);
    }
}
