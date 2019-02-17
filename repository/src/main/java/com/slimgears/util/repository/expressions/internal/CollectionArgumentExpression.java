package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.CollectionExpression;

@AutoValue
public abstract class CollectionArgumentExpression<S, T> implements CollectionExpression<S, T> {
    @JsonCreator
    public static <S, T> CollectionArgumentExpression<S, T> create(
            @JsonProperty("type") Type type) {
        return new AutoValue_CollectionArgumentExpression<>(type);
    }
}
