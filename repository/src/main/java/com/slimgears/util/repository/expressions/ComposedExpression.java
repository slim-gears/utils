package com.slimgears.util.repository.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.util.repository.expressions.internal.BooleanComposedExpression;
import com.slimgears.util.repository.expressions.internal.CollectionComposedExpression;
import com.slimgears.util.repository.expressions.internal.ComparableComposedExpression;
import com.slimgears.util.repository.expressions.internal.NumericComposedExpression;
import com.slimgears.util.repository.expressions.internal.ObjectComposedExpression;
import com.slimgears.util.repository.expressions.internal.StringComposedExpression;

import java.util.Collection;

public interface ComposedExpression<S, T, R> extends ObjectExpression<S, R> {
    @JsonProperty ObjectExpression<S, T> source();
    @JsonProperty ObjectExpression<T, R> expression();

    static <S, T, R> ObjectExpression<S, R> ofObject(ObjectExpression<S, T> source, ObjectExpression<T, R> expression) {
        return ObjectComposedExpression.create(Type.Composition, source, expression);
    }

    static <S, T, R> CollectionExpression<S, R> ofCollection(ObjectExpression<S, T> source, ObjectExpression<T, Collection<R>> expression) {
        return CollectionComposedExpression.create(Type.CollectionComposition, source, expression);
    }

    static <S, T, N extends Number & Comparable<N>> NumericExpression<S, N> ofNumeric(ObjectExpression<S, T> source, ObjectExpression<T, N> expression) {
        return NumericComposedExpression.create(Type.NumericComposition, source, expression);
    }

    static <S, T, R extends Comparable<R>> ComparableExpression<S, R> ofComparable(ObjectExpression<S, T> source, ObjectExpression<T, R> expression) {
        return ComparableComposedExpression.create(Type.ComparableComposition, source, expression);
    }

    static <S, T, R extends Comparable<R>> StringExpression<S> ofString(ObjectExpression<S, T> source, ObjectExpression<T, String> expression) {
        return StringComposedExpression.create(Type.StringComposition, source, expression);
    }

    static <S, T> BooleanExpression<S> ofBoolean(ObjectExpression<S, T> source, ObjectExpression<T, Boolean> expression) {
        return BooleanComposedExpression.create(Type.BooleanComposition, source, expression);
    }
}
