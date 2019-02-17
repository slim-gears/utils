package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;
import com.slimgears.util.repository.expressions.StringExpression;

@AutoValue
public abstract class StringPropertyExpression<S, T, B> implements PropertyExpression<S, T, B, String>, StringExpression<S> {
    @JsonCreator
    public static <S, T, B> StringPropertyExpression<S, T, B> create(
            @JsonProperty("type") Type type,
            @JsonProperty("target") ObjectExpression<S, T> target,
            @JsonProperty("property") PropertyMeta<T, B, ? extends String> property) {
        return new AutoValue_StringPropertyExpression<>(type, target, property);
    }
}
