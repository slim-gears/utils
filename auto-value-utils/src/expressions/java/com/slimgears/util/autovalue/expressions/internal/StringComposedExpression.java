package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.ComposedExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.StringExpression;

@AutoValue
public abstract class StringComposedExpression<S, T> implements ComposedExpression<S, T, String>, StringExpression<S> {
    @JsonCreator
    public static <S, T> StringComposedExpression<S, T> create(
            @JsonProperty("type") Type type,
            @JsonProperty("source") ObjectExpression<S, T> source,
            @JsonProperty("expression") ObjectExpression<T, String> expression) {
        return new AutoValue_StringComposedExpression<>(type, source, expression);
    }
}
