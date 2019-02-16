package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.expressions.internal.ArgumentExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanUnaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.CollectionPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.ComparablePropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.ObjectPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.StringPropertyExpression;

import java.util.Collection;

public interface ObjectExpression<S, T> extends Expression {
    default BooleanExpression<S> eq(ObjectExpression<S, T> value) {
        return BooleanBinaryOperationExpression.create(Type.Equals, this, value);
    }

    default BooleanExpression<S> eq(T value) {
        return eq(ConstantExpression.of(value));
    }

    default BooleanExpression<S> notEq(ObjectExpression<S, T> value) {
        return eq(value).not();
    }

    default BooleanExpression<S> notEq(T value) {
        return eq(value).not();
    }

    default BooleanExpression<S> isNull() {
        return BooleanUnaryOperationExpression.create(Type.IsNull, this);
    }

    default BooleanExpression<S> isNotNull() {
        return isNull().not();
    }

    default BooleanExpression<S> in(T... values) {
        return in(ConstantExpression.of(values));
    }

    default BooleanExpression<S> in(ObjectExpression<S, Collection<T>> values) {
        return BooleanBinaryOperationExpression.create(Type.ValueIn, this, values);
    }

    default BooleanExpression<S> in(Collection<T> values) {
        return in(ConstantExpression.of(values));
    }

    default <R> ObjectExpression<S, R> compose(ObjectExpression<T, R> expression) {
        return ComposedExpression.ofObject(this, expression);
    }

    default <R> CollectionExpression<S, R> compose(CollectionExpression<T, R> expression) {
        return ComposedExpression.ofCollection(this, expression);
    }

    default <R extends Comparable<R>> ComparableExpression<S, R> compose(ComparableExpression<T, R> expression) {
        return ComposedExpression.ofComparable(this, expression);
    }

    default <N extends Number & Comparable<N>> NumericExpression<S, N> compose(NumericExpression<T, N> expression) {
        return ComposedExpression.ofNumeric(this, expression);
    }

    default BooleanExpression<S> compose(BooleanExpression<T> expression) {
        return ComposedExpression.ofBoolean(this, expression);
    }

    default StringExpression<S> compose(StringExpression<T> expression) {
        return ComposedExpression.ofString(this, expression);
    }

    default <B, V> ObjectExpression<S, V> ref(ObjectPropertyExpression<S, T, B, V> expression) {
        return PropertyExpression.ofObject(this, expression.property());
    }

    default <B, V extends Comparable<V>> ComparableExpression<S, V> ref(ComparablePropertyExpression<?, T, B, V> expression) {
        return PropertyExpression.ofComparable(this, expression.property());
    }

    default <B, V extends Number & Comparable<V>> NumericExpression<S, V> ref(NumericPropertyExpression<?, T, B, V> expression) {
        return PropertyExpression.ofNumeric(this, expression.property());
    }

    default <B> BooleanExpression<S> ref(BooleanPropertyExpression<?, T, B> expression) {
        return PropertyExpression.ofBoolean(this, expression.property());
    }

    default <B> StringExpression<S> ref(StringPropertyExpression<?, T, B> expression) {
        return PropertyExpression.ofString(this, expression.property());
    }

    default <B, E> CollectionExpression<S, E> ref(CollectionPropertyExpression<?, T, B, E> expression) {
        return PropertyExpression.ofCollection(this, expression.property());
    }

    default BooleanExpression<S> matches(ObjectExpression<S, String> pattern) {
        return BooleanBinaryOperationExpression.create(Type.Matches, this, pattern);
    }

    default BooleanExpression<S> matches(String pattern) {
        return matches(ConstantExpression.of(pattern));
    }

    static <S, T> ObjectExpression<S, T> arg() {
        return ArgumentExpression.create(Type.Argument);
    }
}
