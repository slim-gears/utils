package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.ConstantExpression;
import com.slimgears.util.autovalue.expressions.StringExpression;

@AutoValue
public abstract class StringConstantExpression implements ConstantExpression<String>, StringExpression {
    @JsonCreator
    public static StringConstantExpression create(@JsonProperty("type") Type type, @JsonProperty String value) {
        return new AutoValue_StringConstantExpression(type, value);
    }
}
