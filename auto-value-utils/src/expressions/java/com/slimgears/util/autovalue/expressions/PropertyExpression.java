package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

public interface PropertyExpression<T, B, V> extends Expression<V> {
    Expression<T> target();
    PropertyMeta<T, B, V> property();

    static <T, B, V> PropertyExpression<T, B, V> of(Expression<T> target, PropertyMeta<T, B, V> property) {
        return new PropertyExpression<T, B, V>() {
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
                return ExpressionType.Property;
            }
        };
    }

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V extends Comparable<V>> ComparablePropertyExpression<T, TB, V> ofComparable(Expression<T> target, PropertyMeta<T, TB, V> property) {
        return ComparablePropertyExpression.of(target, property);
    }

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>> StringPropertyExpression<T, TB> ofString(Expression<T> target, PropertyMeta<T, TB, String> property) {
        return StringPropertyExpression.of(target, property);
    }

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V extends Number & Comparable<V>> NumericPropertyExpression<T, TB, V> ofNumeric(Expression<T> target, PropertyMeta<T, TB, V> property) {
        return NumericPropertyExpression.of(target, property);
    }
}
