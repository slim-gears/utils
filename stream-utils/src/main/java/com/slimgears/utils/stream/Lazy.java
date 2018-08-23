package com.slimgears.utils.stream;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.slimgears.utils.stream.Optionals.ofType;

public class Lazy<T> implements Supplier<T>, AutoCloseable {
    private final Callable<T> supplier;
    private final Object lock = new Object();
    private T instance;

    private Lazy(Callable<T> supplier) {
        this.supplier = supplier;
    }

    public void ifExists(Consumer<T> consumer) {
        synchronized (lock) {
            Optional.ofNullable(instance)
                    .ifPresent(consumer);
        }
    }

    public <R> Lazy<R> map(Function<T, R> mapper) {
        return Lazy.of(() -> mapper.apply(get()));
    }

    @Override
    public T get() {
        return Optional
                .ofNullable(instance)
                .orElseGet(Synchornized
                        .withLock(lock)
                        .ofUnsafe(() -> Optional
                                .ofNullable(instance)
                                .orElseGet(Safe.ofSupplier(() -> instance = supplier.call()))));
    }

    public void close() {
        Synchornized
                .withLock(lock)
                .of(() -> Optional
                        .ofNullable(instance)
                        .<AutoCloseable>flatMap(ofType(AutoCloseable.class))
                        .ifPresent(Safe.ofConsumer(AutoCloseable::close)));
    }

    public static <T> Lazy<T> fromCallable(Callable<T> callable) {
        return new Lazy<>(callable);
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return fromCallable(supplier::get);
    }
}
