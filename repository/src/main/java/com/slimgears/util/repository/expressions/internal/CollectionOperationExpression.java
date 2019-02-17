package com.slimgears.util.repository.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.CollectionExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;

import java.util.Collection;

@AutoValue
public abstract class CollectionOperationExpression<S, T, M, R> implements CollectionExpression<S, R> {
    @JsonProperty public abstract ObjectExpression<S, Collection<T>> source();
    @JsonProperty public abstract ObjectExpression<T, M> operation();

    @JsonCreator
    public static <S, T, M, R> CollectionOperationExpression<S, T, M, R> create(
            @JsonProperty("type") Type type,
            @JsonProperty("source") ObjectExpression<S, Collection<T>> source,
            @JsonProperty("operation") ObjectExpression<T, M> operation) {
        return new AutoValue_CollectionOperationExpression<>(type, source, operation);
    }
}
