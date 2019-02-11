package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;
import com.slimgears.util.autovalue.expressions.StringExpression;

@AutoValue
public abstract class StringPropertyExpression<T, B> implements PropertyExpression<T, B, String>, StringExpression {
    @JsonCreator
    public static <T, B> StringPropertyExpression<T, B> create(
            @JsonProperty("type") Type type,
            @JsonProperty("target") ObjectExpression<T> target,
            @JsonProperty("property") PropertyMeta<T, B, String> property) {
        return new AutoValue_StringPropertyExpression<>(type, target, property);
    }
}
