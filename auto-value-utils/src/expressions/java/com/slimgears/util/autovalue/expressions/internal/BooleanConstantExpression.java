package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.BooleanExpression;
import com.slimgears.util.autovalue.expressions.ConstantExpression;
import com.slimgears.util.autovalue.expressions.NumericExpression;

@AutoValue
public abstract class BooleanConstantExpression<S> implements ConstantExpression<S, Boolean>, BooleanExpression<S> {
    @JsonCreator
    public static <S> BooleanConstantExpression<S> create(
            @JsonProperty("type") Type type,
            @JsonProperty("value") boolean value) {
        return new AutoValue_BooleanConstantExpression<>(type, value);
    }
}
