package com.slimgears.util.autovalue.annotations;

public interface HasMetaClass<T, B extends BuilderPrototype<T, B>> {
    MetaClass<T, B> metaClass();
}
