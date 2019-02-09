package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

public interface ComparablePropertyExpression<T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V extends Comparable<V>> extends PropertyExpression<T, TB, V>, ComparableValueExpression<V> {
    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>, V extends Comparable<V>> ComparablePropertyExpression<T, TB, V> of(PropertyMeta<T, TB, V> property) {
        return new ComparablePropertyExpression<T, TB, V>() {
            @Override
            public PropertyMeta<T, TB, V> property() {
                return property;
            }

            @Override
            public String type() {
                return "comparableProperty";
            }
        };
    }
}
