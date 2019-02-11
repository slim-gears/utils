package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.ComparableExpression;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;

@AutoValue
public abstract class ComparablePropertyExpression<T, B, V extends Comparable<V>> implements PropertyExpression<T, B, V>, ComparableExpression<V> {
    @JsonCreator
    public static <T, B, V extends Comparable<V>> ComparablePropertyExpression<T, B, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("target") ObjectExpression<T> target,
            @JsonProperty("property") PropertyMeta<T, B, V> property) {
        return new AutoValue_ComparablePropertyExpression<>(type, target, property);
    }
}
