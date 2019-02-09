package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

public interface PropertyExpression<T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> extends ValueExpression<V> {
    PropertyMeta<T, TB, V> property();

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V> PropertyExpression<T, TB, V> of(PropertyMeta<T, TB, V> property) {
        return new PropertyExpression<T, TB, V>() {
            @Override
            public PropertyMeta<T, TB, V> property() {
                return property;
            }

            @Override
            public String type() {
                return "property";
            }
        };
    }

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V extends Comparable<V>> ComparablePropertyExpression<T, TB, V> ofComparable(PropertyMeta<T, TB, V> property) {
        return ComparablePropertyExpression.of(property);
    }

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>> StringPropertyExpression<T, TB> ofString(PropertyMeta<T, TB, String> property) {
        return StringPropertyExpression.of(property);
    }

    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V extends Number & Comparable<V>> NumericPropertyExpression<T, TB, V> ofNumeric(PropertyMeta<T, TB, V> property) {
        return NumericPropertyExpression.of(property);
    }
}
