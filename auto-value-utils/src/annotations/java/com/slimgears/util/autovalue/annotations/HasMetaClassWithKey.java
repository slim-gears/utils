package com.slimgears.util.autovalue.annotations;

public interface HasMetaClassWithKey<K, T, TB extends BuilderPrototype<T, TB>> extends HasMetaClass<T, TB> {
    MetaClassWithKey<K, T, TB> metaClass();
}
