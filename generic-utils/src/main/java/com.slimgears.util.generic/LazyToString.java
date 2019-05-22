package com.slimgears.util.generic;

import java.util.Optional;
import java.util.function.Supplier;

public class LazyToString {
    private final Supplier<Object> supplier;

    private LazyToString(Supplier<Object> supplier) {
        this.supplier = supplier;
    }

    public static Object lazy(Supplier<Object> supplier) {
        return new LazyToString(supplier);
    }

    @Override
    public String toString() {
        return Optional
                .ofNullable(supplier.get())
                .map(Object::toString)
                .orElse(null);
    }
}
