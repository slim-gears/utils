package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

@AutoValue
public abstract class ArgumentExpression<T> implements ObjectExpression<T> {
    public abstract @JsonProperty String name();

    @JsonCreator
    public static <T> ArgumentExpression<T> create(
            @JsonProperty("type") Type type,
            @JsonProperty("name") String name) {
        return new AutoValue_ArgumentExpression<>(type, name);
    }
}
