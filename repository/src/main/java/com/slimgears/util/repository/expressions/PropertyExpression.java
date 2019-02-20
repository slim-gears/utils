package com.slimgears.util.repository.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.repository.expressions.internal.*;

import java.util.Collection;

public interface PropertyExpression<S, T, B, V> extends ObjectExpression<S, V> {
    @JsonProperty ObjectExpression<S, T> target();
    @JsonProperty PropertyMeta<T, B, ? extends V> property();

    static <S, T, B, V> ObjectPropertyExpression<S, T, B, V> ofObject(ObjectExpression<S, T> target, PropertyMeta<T, B, ? extends V> property) {
        return ObjectPropertyExpression.create(Type.Property, target, property);
    }

    static <S, B, V> ObjectPropertyExpression<S, S, B, V> ofObject(PropertyMeta<S, B, ? extends V> property) {
        return ofObject(ObjectExpression.arg(), property);
    }

    static <S, T, B, V extends Comparable<V>> ComparablePropertyExpression<S, T, B, V> ofComparable(ObjectExpression<S, T> target, PropertyMeta<T, B, ? extends V> property) {
        return ComparablePropertyExpression.create(Type.ComparableProperty, target, property);
    }

    static <S, B, V extends Comparable<V>> ComparablePropertyExpression<S, S, B, V> ofComparable(PropertyMeta<S, B, ? extends V> property) {
        return ofComparable(ObjectExpression.arg(), property);
    }

    static <S, T, B> StringPropertyExpression<S, T, B> ofString(ObjectExpression<S, T> target, PropertyMeta<T, B, ? extends String> property) {
        return StringPropertyExpression.create(Type.StringProperty, target, property);
    }

    static <S, B> StringPropertyExpression<S, S, B> ofString(PropertyMeta<S, B, ? extends String> property) {
        return ofString(ObjectExpression.arg(), property);
    }

    static <S, T, B> BooleanPropertyExpression<S, T, B> ofBoolean(ObjectExpression<S, T> target, PropertyMeta<T, B, ? extends Boolean> property) {
        return BooleanPropertyExpression.create(Type.BooleanProperty, target, property);
    }

    static <S, B> BooleanPropertyExpression<S, S, B> ofBoolean(PropertyMeta<S, B, ? extends Boolean> property) {
        return ofBoolean(ObjectExpression.arg(), property);
    }

    static <S, T, B, V extends Number & Comparable<V>> NumericPropertyExpression<S, T, B, V> ofNumeric(ObjectExpression<S, T> target, PropertyMeta<T, B, ? extends V> property) {
        return NumericPropertyExpression.create(Type.NumericProperty, target, property);
    }

    static <S, B, V extends Number & Comparable<V>> NumericPropertyExpression<S, S, B, V> ofNumeric(PropertyMeta<S, B, ? extends V> property) {
        return ofNumeric(ObjectExpression.arg(), property);
    }

    static <S, T, B, E> CollectionPropertyExpression<S, T, B, E> ofCollection(ObjectExpression<S, T> target, PropertyMeta<T, B, ? extends Collection<E>> property) {
        return CollectionPropertyExpression.create(Type.CollectionProperty, target, property);
    }

    static <S, B, E> CollectionPropertyExpression<S, S, B, E> ofCollection(PropertyMeta<S, B, ? extends Collection<E>> property) {
        return ofCollection(ObjectExpression.arg(), property);
    }
}
