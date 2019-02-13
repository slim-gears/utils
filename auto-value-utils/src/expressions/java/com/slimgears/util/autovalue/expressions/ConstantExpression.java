package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.util.autovalue.expressions.internal.BooleanConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.CollectionConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.ObjectConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.StringConstantExpression;

import java.util.Arrays;
import java.util.Collection;

public interface ConstantExpression<S, T> extends ObjectExpression<S, T> {
    @JsonProperty T value();

    static <S, V> ConstantExpression<S, V> of(V value) {
        return ObjectConstantExpression.create(Type.Constant, value);
    }

    static <S, V extends Number & Comparable<V>> NumericConstantExpression<S, V> of(V value) {
        return NumericConstantExpression.create(Type.NumericConstant, value);
    }

    static <S> StringConstantExpression<S> of(String value) {
        return StringConstantExpression.create(Type.StringConstant, value);
    }

    static <S> BooleanConstantExpression<S> of(boolean value) {
        return BooleanConstantExpression.create(Type.BooleanConstant, value);
    }

    static <S, E> CollectionConstantExpression<S, E> of(Collection<E> collection) {
        return CollectionConstantExpression.create(Type.CollectionConstant, collection);
    }

    @SafeVarargs
    static <S, E> CollectionConstantExpression<S, E> of(E... items) {
        return of(Arrays.asList(items));
    }
}
