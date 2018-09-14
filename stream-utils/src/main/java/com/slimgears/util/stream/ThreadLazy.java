package com.slimgears.util.stream;

import java.util.function.Supplier;

public class ThreadLazy<T> extends ThreadLocal<T> implements Supplier<T> {
    private final Supplier<T> supplier;

    private ThreadLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        T instance = super.get();
        if (instance == null) {
            instance = supplier.get();
            set(instance);
        }
        return instance;
    }

    public static <T> ThreadLazy<T> of(Supplier<T> supplier) {
        return new ThreadLazy<>(supplier);
    }
}
