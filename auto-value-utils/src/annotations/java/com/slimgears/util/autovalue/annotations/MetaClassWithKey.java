package com.slimgears.util.autovalue.annotations;

public interface MetaClassWithKey<K, T> extends MetaClass<T>, HasKeyProperty<K, T> {
    default K keyOf(T value) {
        return keyProperty().getValue(value);
    }
}
