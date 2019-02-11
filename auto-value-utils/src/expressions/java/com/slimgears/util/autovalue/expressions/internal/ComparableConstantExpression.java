package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.ComparableExpression;
import com.slimgears.util.autovalue.expressions.ConstantExpression;

@AutoValue
public abstract class ComparableConstantExpression<V extends Comparable<V>> implements ConstantExpression<V>, ComparableExpression<V> {
    @JsonCreator
    public static <V extends Comparable<V>> ComparableConstantExpression<V> create(
            @JsonProperty Type type,
            @JsonProperty V value) {
        return new AutoValue_ComparableConstantExpression<>(type, value);
    }
}
