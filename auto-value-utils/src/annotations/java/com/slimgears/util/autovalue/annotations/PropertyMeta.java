package com.slimgears.util.autovalue.annotations;

import com.slimgears.util.reflect.TypeToken;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface PropertyMeta<T, B, V> {
    String name();
    TypeToken<V> type();
    void setValue(B builder, V value);
    V getValue(T instance);

    default void mergeValue(B builder, T instance) {
        Optional
                .ofNullable(getValue(instance))
                .ifPresent(val -> setValue(builder, val));
    }

    static <T, B, V> PropertyMeta<T, B, V> create(String name, TypeToken<V> type, Function<T, V> getter, BiConsumer<B, V> setter) {
        return new PropertyMeta<T, B, V>() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public TypeToken<V> type() {
                return type;
            }

            @Override
            public void setValue(B builder, V value) {
                setter.accept(builder, value);
            }

            @Override
            public V getValue(T instance) {
                return getter.apply(instance);
            }
        };
    }
}
