package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.ComparableExpression;
import com.slimgears.util.repository.expressions.ComposedExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;

@AutoValue
public abstract class ComparableComposedExpression<S, T, R extends Comparable<R>> implements ComposedExpression<S, T, R>, ComparableExpression<S, R> {
    @JsonCreator
    public static <S, T, R extends Comparable<R>> ComparableComposedExpression<S, T, R> create(
            @JsonProperty("type") Type type,
            @JsonProperty("source") ObjectExpression<S, T> source,
            @JsonProperty("expression") ObjectExpression<T, R> expression) {
        return new AutoValue_ComparableComposedExpression<>(type, source, expression);
    }
}
