package com.slimgears.util.autovalue.annotations;

public interface BuilderPrototype<T, B extends BuilderPrototype<T, B>> {
    T build();
    default B self() {
        //noinspection unchecked
        return (B)this;
    }
}
