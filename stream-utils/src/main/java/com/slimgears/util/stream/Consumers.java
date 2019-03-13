package com.slimgears.util.stream;

import java.util.Arrays;
import java.util.function.Consumer;

public class Consumers {
    @SafeVarargs
    public static <T> T configure(T instance, Consumer<T>... configs) {
        return configure(instance, combine(configs));
    }

    public static <T> T configure(T instance, Consumer<T> config) {
        config.accept(instance);
        return instance;
    }

    @SafeVarargs
    public static <T> Consumer<T> combine(Consumer<T>... configs) {
        return obj -> Arrays.asList(configs).forEach(c -> c.accept(obj));
    }
}
