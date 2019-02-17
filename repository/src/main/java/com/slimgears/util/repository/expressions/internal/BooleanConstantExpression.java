package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.BooleanExpression;
import com.slimgears.util.repository.expressions.ConstantExpression;

@AutoValue
public abstract class BooleanConstantExpression<S> implements ConstantExpression<S, Boolean>, BooleanExpression<S> {
    @JsonCreator
    public static <S> BooleanConstantExpression<S> create(
            @JsonProperty("type") Type type,
            @JsonProperty("value") boolean value) {
        return new AutoValue_BooleanConstantExpression<>(type, value);
    }
}
