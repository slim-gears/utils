package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.NumericExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;

@AutoValue
public abstract class NumericPropertyExpression<T, B, V extends Number & Comparable<V>> implements PropertyExpression<T, B, V>, NumericExpression<V> {
    @JsonCreator
    public static <T, B, V extends Number & Comparable<V>> NumericPropertyExpression<T, B, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("target") ObjectExpression<T> target,
            @JsonProperty("property") PropertyMeta<T, B, V> property) {
        return new AutoValue_NumericPropertyExpression<>(type, target, property);
    }
}
