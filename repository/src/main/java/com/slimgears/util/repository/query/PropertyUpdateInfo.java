package com.slimgears.util.repository.query;

import com.google.auto.value.AutoValue;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.PropertyExpression;

@AutoValue
public abstract class PropertyUpdateInfo<S, T, TB, V> {
    public abstract PropertyExpression<S, T, TB, V> property();
    public abstract ObjectExpression<S, V> updater();

    public static <S, T, TB, V> PropertyUpdateInfo<S, T, TB, V> create(PropertyExpression<S, T, TB, V> property, ObjectExpression<S, V> updater) {
        return new AutoValue_PropertyUpdateInfo<>(property, updater);
    }
}
