package com.slimgears.util.autovalue.annotations;

public interface HasMetaClassWithKey<K, T> extends HasMetaClass<T> {
    MetaClassWithKey<K, T> metaClass();

    static <K, T extends HasMetaClassWithKey<K, T>> K keyOf(T value) {
        return value.metaClass().keyOf(value);
    }
}
