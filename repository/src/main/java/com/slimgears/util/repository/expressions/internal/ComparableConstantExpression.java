package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.ComparableExpression;
import com.slimgears.util.repository.expressions.ConstantExpression;

@AutoValue
public abstract class ComparableConstantExpression<S, V extends Comparable<V>> implements ConstantExpression<S, V>, ComparableExpression<S, V> {
    @JsonCreator
    public static <S, V extends Comparable<V>> ComparableConstantExpression<S, V> create(
            @JsonProperty Type type,
            @JsonProperty V value) {
        return new AutoValue_ComparableConstantExpression<>(type, value);
    }
}
