package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.ComposedExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;

@AutoValue
public abstract class ObjectComposedExpression<S, T, R> implements ComposedExpression<S, T, R>, ObjectExpression<S, R> {
    @JsonCreator
    public static <S, T, R> ObjectComposedExpression<S, T, R> create(
            @JsonProperty("type") Type type,
            @JsonProperty("source") ObjectExpression<S, T> source,
            @JsonProperty("expression") ObjectExpression<T, R> expression) {
        return new AutoValue_ObjectComposedExpression<>(type, source, expression);
    }
}
