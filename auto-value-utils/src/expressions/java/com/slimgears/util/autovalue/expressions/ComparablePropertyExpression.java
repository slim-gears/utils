package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.annotations.PropertyMeta;

public interface ComparablePropertyExpression<T, B, V extends Comparable<V>> extends PropertyExpression<T, B, V>, ComparableExpression<V> {
    static <T, B, V extends Comparable<V>> ComparablePropertyExpression<T, B, V> of(Expression<T> target, PropertyMeta<T, B, V> property) {
        return new ComparablePropertyExpression<T, B, V>() {
            @Override
            public Expression<T> target() {
                return target;
            }

            @Override
            public PropertyMeta<T, B, V> property() {
                return property;
            }

            @Override
            public ExpressionType type() {
                return ExpressionType.ComparableProperty;
            }
        };
    }
}
