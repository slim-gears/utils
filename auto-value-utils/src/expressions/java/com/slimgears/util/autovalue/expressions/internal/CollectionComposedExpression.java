package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.CollectionExpression;
import com.slimgears.util.autovalue.expressions.ComposedExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

import java.util.Collection;

@AutoValue
public abstract class CollectionComposedExpression<S, T, R> implements ComposedExpression<S, T, Collection<R>>, CollectionExpression<S, R> {
    @JsonCreator
    public static <S, T, R> CollectionComposedExpression<S, T, R> create(
            @JsonProperty("type") Type type,
            @JsonProperty("source") ObjectExpression<S, T> source,
            @JsonProperty("expression") ObjectExpression<T, Collection<R>> expression) {
        return new AutoValue_CollectionComposedExpression<>(type, source, expression);
    }
}
