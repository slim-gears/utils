package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.internal.ComparablePropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.ObjectPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.StringPropertyExpression;

public interface PropertyExpression<T, B, V> extends ObjectExpression<V> {
    @JsonProperty ObjectExpression<T> target();
    @JsonProperty PropertyMeta<T, B, V> property();

    static <T, B, V> PropertyExpression<T, B, V> ofObject(ObjectExpression<T> target, PropertyMeta<T, B, V> property) {
        return ObjectPropertyExpression.create(Type.Property, target, property);
    }

    static <T, B, V extends Comparable<V>> ComparablePropertyExpression<T, B, V> ofComparable(ObjectExpression<T> target, PropertyMeta<T, B, V> property) {
        return ComparablePropertyExpression.create(Type.ComparableProperty, target, property);
    }

    static <T, B> StringPropertyExpression<T, B> ofString(ObjectExpression<T> target, PropertyMeta<T, B, String> property) {
        return StringPropertyExpression.create(Type.StringProperty, target, property);
    }

    static <T, B, V extends Number & Comparable<V>> NumericPropertyExpression<T, B, V> ofNumeric(ObjectExpression<T> target, PropertyMeta<T, B, V> property) {
        return NumericPropertyExpression.create(Type.NumericProperty, target, property);
    }
}
