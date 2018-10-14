package com.slimgears.sample;

public interface SampleGenericInterface<T> {
    T tValue();

    interface Builder<T, B extends Builder<T, B>> {
        default B customSetter(T val) {
            return (B)this;
        }
    }
}
