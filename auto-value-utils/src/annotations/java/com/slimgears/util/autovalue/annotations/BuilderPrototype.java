package com.slimgears.util.autovalue.annotations;

import java.util.function.Consumer;

public interface BuilderPrototype<T, B extends BuilderPrototype<T, B>> {
    T build();

    @SuppressWarnings("unchecked")
    default B self() {
        return (B)this;
    }

    default B apply(Consumer<? super B> config) {
        config.accept(self());
        return self();
    }
}
