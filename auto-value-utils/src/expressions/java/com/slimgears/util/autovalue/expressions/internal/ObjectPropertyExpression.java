package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;

@AutoValue
public abstract class ObjectPropertyExpression<S, T, B, V> implements PropertyExpression<S, T, B, V> {
    @JsonCreator
    public static <S, T, B, V> ObjectPropertyExpression<S, T, B, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("target") ObjectExpression<S, T> target,
            @JsonProperty("property") PropertyMeta<T, B, ? extends V> property) {
        return new AutoValue_ObjectPropertyExpression<>(type, target, property);
    }
}
