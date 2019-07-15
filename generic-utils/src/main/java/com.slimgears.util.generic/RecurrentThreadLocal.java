package com.slimgears.util.generic;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RecurrentThreadLocal<T> {
    private final ThreadLocal<ObjectHolder<T>> holder;
    private final Supplier<T> factory;
    private final Consumer<T> onRelease;

    private RecurrentThreadLocal(Supplier<T> factory, Consumer<T> onRelease) {
        this.holder = ThreadLocal.withInitial(() -> new ObjectHolder<>(factory.get()));
        this.factory = factory;
        this.onRelease = onRelease;
    }

    public static <T> RecurrentThreadLocal<T> of(Supplier<T> factory) {
        return new RecurrentThreadLocal<>(factory, item -> {});
    }

    public RecurrentThreadLocal<T> onRelease(Consumer<T> runOnRelease) {
        return new RecurrentThreadLocal<>(factory, this.onRelease.andThen(runOnRelease));
    }

    public T acquire() {
        return this.holder.get().acquire();
    }

    public void release() {
        ObjectHolder<T> holder = Optional
                .ofNullable(this.holder.get())
                .orElseThrow(() -> new RuntimeException("Cannot release object: object was not acquired"));
        if (!holder.release()) {
            this.holder.remove();
            T instance = holder.object;
            this.onRelease.accept(instance);
        }
    }

    private static class ObjectHolder<T> {
        private final T object;
        private int counter;

        private ObjectHolder(T object) {
            this.object = object;
        }

        private T acquire() {
            ++counter;
            return object;
        }

        private boolean release() {
            return --counter > 0;
        }
    }

}
