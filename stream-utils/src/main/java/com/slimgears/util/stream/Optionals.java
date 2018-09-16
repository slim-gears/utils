package com.slimgears.util.stream;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Optionals {
    public static <T, R extends T> Function<T, Optional<R>> ofType(Class<R> type) {
        return val -> Optional
                .ofNullable(val)
                .filter(type::isInstance)
                .map(type::cast);
    }

    @SafeVarargs
    public static <T> Optional<T> or(Supplier<Optional<T>>... variants) {
        return Stream.of(variants)
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }
}
