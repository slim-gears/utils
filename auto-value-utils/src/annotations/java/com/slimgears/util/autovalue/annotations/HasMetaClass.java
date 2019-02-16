package com.slimgears.util.autovalue.annotations;

public interface HasMetaClass<S, B extends BuilderPrototype<S, B>> {
    MetaClass<S, B> metaClass();

    interface Builder<_B extends Builder<_B, S, B>, S, B extends BuilderPrototype<S, B>> {
        _B metaClass(MetaClass<S, B> metaClass);
    }
}
