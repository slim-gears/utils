package com.slimgears.util.autovalue.annotations;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class AtomicLazy<T> implements Supplier<T> {
    private final Callable<T> supplier;
    private final AtomicReference<T> reference = new AtomicReference<>();

    private AtomicLazy(Callable<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Supplier<T> of(Callable<T> supplier) {
        return new AtomicLazy<>(supplier);
    }

    @Override
    public T get() {
        return Optional
                .ofNullable(reference.get())
                .orElseGet(() -> {
                    try {
                        reference.compareAndSet(null, supplier.call());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return reference.get();
                });
    }
}
