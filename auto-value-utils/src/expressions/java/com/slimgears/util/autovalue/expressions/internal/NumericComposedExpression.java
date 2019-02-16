package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.ComposedExpression;
import com.slimgears.util.autovalue.expressions.NumericExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

@AutoValue
public abstract class NumericComposedExpression<S, T, N extends Number & Comparable<N>> implements ComposedExpression<S, T, N>, NumericExpression<S, N> {
    @JsonCreator
    public static <S, T, N extends Number & Comparable<N>> NumericComposedExpression<S, T, N> create(
            @JsonProperty("type") Type type,
            @JsonProperty("source") ObjectExpression<S, T> source,
            @JsonProperty("expression") ObjectExpression<T, N> expression) {
        return new AutoValue_NumericComposedExpression<>(type, source, expression);
    }
}
