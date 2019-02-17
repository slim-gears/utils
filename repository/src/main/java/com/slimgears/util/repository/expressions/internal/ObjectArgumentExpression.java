package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.ObjectExpression;

@AutoValue
public abstract class ObjectArgumentExpression<S, T> implements ObjectExpression<S, T> {
    @JsonCreator
    public static <S, T> ObjectArgumentExpression<S, T> create(
            @JsonProperty("type") Type type) {
        return new AutoValue_ObjectArgumentExpression<>(type);
    }
}
