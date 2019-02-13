package com.slimgears.util.autovalue.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.expressions.CollectionExpression;
import com.slimgears.util.autovalue.expressions.ConstantExpression;

import java.util.Collection;

@AutoValue
public abstract class CollectionConstantExpression<S, E> implements ConstantExpression<S, Collection<E>>, CollectionExpression<S, E> {
    @JsonCreator
    public static <S, E> CollectionConstantExpression<S, E> create(@JsonProperty("type") Type type, @JsonProperty Collection<E> value) {
        return new AutoValue_CollectionConstantExpression<>(type, value);
    }
}
