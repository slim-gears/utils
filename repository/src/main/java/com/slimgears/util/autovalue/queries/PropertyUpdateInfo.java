package com.slimgears.util.autovalue.queries;

import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

@AutoValue
public abstract class PropertyUpdateInfo<T, V> {
    public abstract PropertyMeta<T, ?, ? extends V> property();
    public abstract ObjectExpression<T, V> updater();

    public static <T, V> PropertyUpdateInfo<T, V> create(PropertyMeta<T, ?, ? extends V> property, ObjectExpression<T, V> updater) {
        return new AutoValue_PropertyUpdateInfo<>(property, updater);
    }
}
