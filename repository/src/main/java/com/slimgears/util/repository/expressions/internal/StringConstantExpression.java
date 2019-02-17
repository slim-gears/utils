package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.ConstantExpression;
import com.slimgears.util.repository.expressions.StringExpression;

@AutoValue
public abstract class StringConstantExpression<S> implements ConstantExpression<S, String>, StringExpression<S> {
    @JsonCreator
    public static <S> StringConstantExpression<S> create(@JsonProperty("type") Type type, @JsonProperty String value) {
        return new AutoValue_StringConstantExpression<>(type, value);
    }
}
