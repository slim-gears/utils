package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.BooleanExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;
import com.slimgears.util.autovalue.expressions.StringExpression;

@AutoValue
public abstract class BooleanPropertyExpression<S, T, B> implements PropertyExpression<S, T, B, Boolean>, BooleanExpression<S> {
    @JsonCreator
    public static <S, T, B> BooleanPropertyExpression<S, T, B> create(
            @JsonProperty("type") Type type,
            @JsonProperty("target") ObjectExpression<S, T> target,
            @JsonProperty("property") PropertyMeta<T, B, Boolean> property) {
        return new AutoValue_BooleanPropertyExpression<>(type, target, property);
    }
}
