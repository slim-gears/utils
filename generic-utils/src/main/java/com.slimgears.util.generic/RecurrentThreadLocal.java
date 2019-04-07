package com.slimgears.util.generic;

import java.util.Optional;
import java.util.function.Supplier;

public class RecurrentThreadLocal<T> {
    private final ThreadLocal<ObjectHolder<T>> holder;

    public RecurrentThreadLocal(Supplier<T> factory) {
        this.holder = ThreadLocal.withInitial(() -> new ObjectHolder<>(factory.get()));
    }

    public T acquire() {
        return this.holder.get().acquire();
    }

    public void release() {
        if (!Optional
                .ofNullable(this.holder.get())
                .orElseThrow(() -> new RuntimeException("Cannot release object: object was not acquired"))
                .release()) {
            this.holder.remove();
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
