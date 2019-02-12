package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

@AutoValue
public abstract class ArgumentExpression<S, T> implements ObjectExpression<S, T> {
    @JsonCreator
    public static <S, T> ArgumentExpression<S, T> create(
            @JsonProperty("type") Type type) {
        return new AutoValue_ArgumentExpression<>(type);
    }
}
