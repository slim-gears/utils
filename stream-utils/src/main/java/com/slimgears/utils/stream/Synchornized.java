package com.slimgears.utils.stream;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Synchornized {
    interface SynchronizedInvoker {
        <T> Supplier<T> of(Supplier<T> supplier);
        <T> Supplier<T> ofUnsafe(Callable<T> callable);
        Runnable ofUnsafe(Safe.UnsafeRunnable runnable);
        Runnable of(Runnable runnable);
        <T, R> Function<T, R> of(Function<T, R> func);
        <T, U, R> BiFunction<T, U, R> of(BiFunction<T, U, R> func);
    }

    public static <T> T withLock(Object lock, Callable<T> callable) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (lock) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static SynchronizedInvoker withLock(Object lock) {
        return new SynchronizedInvoker() {
            @Override
            public <T> Supplier<T> of(Supplier<T> supplier) {
                return () -> withLock(lock, supplier::get);
            }

            @Override
            public <T> Supplier<T> ofUnsafe(Callable<T> callable) {
                return () -> withLock(lock, callable);
            }

            @Override
            public Runnable ofUnsafe(Safe.UnsafeRunnable runnable) {
                return () -> withLock(lock, () -> { runnable.run(); return null; });
            }

            @Override
            public Runnable of(Runnable runnable) {
                return ofUnsafe(runnable::run);
            }

            @Override
            public <T, R> Function<T, R> of(Function<T, R> func) {
                return arg -> withLock(lock, () -> func.apply(arg));
            }

            @Override
            public <T, U, R> BiFunction<T, U, R> of(BiFunction<T, U, R> func) {
                return (arg1, arg2) -> withLock(lock, () -> func.apply(arg1, arg2));
            }
        };
    }
}
