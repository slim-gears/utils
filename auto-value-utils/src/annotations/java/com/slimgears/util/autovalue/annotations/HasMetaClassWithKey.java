package com.slimgears.util.autovalue.annotations;

public interface HasMetaClassWithKey<K, T> extends HasMetaClass<T> {
    MetaClassWithKey<K, T> metaClass();
}
