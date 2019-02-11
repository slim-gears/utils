package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.ConstantExpression;

@AutoValue
public abstract class ObjectConstantExpression<V> implements ConstantExpression<V> {
    @JsonCreator
    public static <V> ObjectConstantExpression<V> create(
            @JsonProperty Type type,
            @JsonProperty V value) {
        return new AutoValue_ObjectConstantExpression<>(type, value);
    }
}
