package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.StringExpression;
import com.slimgears.util.repository.expressions.UnaryOperationExpression;

@AutoValue
public abstract class StringUnaryOperationExpression<S, T> implements UnaryOperationExpression<S, T, String>, StringExpression<S> {
    @JsonCreator
    public static <S, T> StringUnaryOperationExpression<S, T> create(
            @JsonProperty("type") Type type,
            @JsonProperty("operand") ObjectExpression<S, T> operand) {
        return new AutoValue_StringUnaryOperationExpression<>(type, operand);
    }
}
