package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

public interface NumericPropertyExpression<T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V extends Number & Comparable<V>> extends ComparablePropertyExpression<T, TB, V>, NumericValueExpression<V> {
    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V extends Number & Comparable<V>> NumericPropertyExpression<T, TB, V> of(PropertyMeta<T, TB, V> property) {
        return new NumericPropertyExpression<T, TB, V>() {
            @Override
            public PropertyMeta<T, TB, V> property() {
                return property;
            }

            @Override
            public String type() {
                return "numericProperty";
            }
        };
    }
}
