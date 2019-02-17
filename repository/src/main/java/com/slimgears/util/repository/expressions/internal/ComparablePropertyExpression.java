package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.repository.expressions.ComparableExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;

@AutoValue
public abstract class ComparablePropertyExpression<S, T, B, V extends Comparable<V>> implements PropertyExpression<S, T, B, V>, ComparableExpression<S, V> {
    @JsonCreator
    public static <S, T, B, V extends Comparable<V>> ComparablePropertyExpression<S, T, B, V> create(
            @JsonProperty("type") Type type,
            @JsonProperty("target") ObjectExpression<S, T> target,
            @JsonProperty("property") PropertyMeta<T, B, ? extends V> property) {
        return new AutoValue_ComparablePropertyExpression<>(type, target, property);
    }
}
