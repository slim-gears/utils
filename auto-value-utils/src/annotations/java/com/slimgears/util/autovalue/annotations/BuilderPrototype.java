package com.slimgears.util.autovalue.annotations;

import java.util.function.Consumer;

public interface BuilderPrototype<T, B extends BuilderPrototype<T, B>> extends MetaBuilder<T>, HasSelf<B> {
    @SuppressWarnings("unchecked")
    default B apply(Consumer<? super B> config) {
        config.accept(self());
        return self();
    }
}
