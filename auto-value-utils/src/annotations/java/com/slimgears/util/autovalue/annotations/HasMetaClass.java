package com.slimgears.util.autovalue.annotations;

public interface HasMetaClass<S> {
    MetaClass<S> metaClass();

    interface Builder<_B extends Builder<_B, S>, S> {
        _B metaClass(MetaClass<S> metaClass);
    }
}
