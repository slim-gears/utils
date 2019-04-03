package com.slimgears.util.autovalue.annotations;

import java.util.function.Consumer;

public interface MetaBuilder<T> {
    T build();

    default T build(Consumer<MetaBuilder<T>> initializer) {
        initializer.accept(this);
        return build();
    }
}
