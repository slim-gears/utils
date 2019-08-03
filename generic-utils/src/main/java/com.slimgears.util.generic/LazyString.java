package com.slimgears.util.generic;

import java.util.Optional;
import java.util.function.Supplier;

public class LazyString {
    private final Supplier<?> supplier;

    private LazyString(Supplier<?> supplier) {
        this.supplier = supplier;
    }

    @SuppressWarnings("WeakerAccess")
    public static Object lazy(Supplier<?> supplier) {
        return new LazyString(supplier);
    }

    @Override
    public String toString() {
        try {
            return Optional
                    .ofNullable(supplier.get())
                    .map(Object::toString)
                    .orElse(null);
        } catch (Throwable e) {
            return "{error: '" + e.getMessage() + "'}";
        }
    }
}
