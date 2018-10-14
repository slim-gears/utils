package com.slimgears.util.autovalue.annotations;

public interface BuilderPrototype<T, B extends BuilderPrototype<T, B>> {
    T build();

    @SuppressWarnings("unchecked")
    default B self() {
        return (B)this;
    }
}
