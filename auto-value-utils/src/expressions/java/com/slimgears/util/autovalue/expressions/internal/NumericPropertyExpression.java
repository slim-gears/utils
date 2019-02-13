package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.NumericExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;

@AutoValue
public abstract class NumericPropertyExpression<S, T, B, V extends Number & Comparable<V>> implements PropertyExpression<S, T, B, V>, NumericExpression<S, V> {
    @JsonCreator
    public static <S, T, B, V extends Number & Comparable<V>> NumericPropertyExpression<S, T, B, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("target") ObjectExpression<S, T> target,
            @JsonProperty("property") PropertyMeta<T, B, ? extends V> property) {
        return new AutoValue_NumericPropertyExpression<>(type, target, property);
    }
}
