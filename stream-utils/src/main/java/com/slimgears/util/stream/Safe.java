/**
 *
 */
package com.slimgears.util.stream;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Safe {
    public interface UnsafeSupplier<T> {
        T get() throws Exception;
    }

    public interface UnsafeConsumer<T> {
        void accept(T val) throws Exception;
    }

    public interface UnsafeRunnable {
        void run() throws Exception;
    }

    public interface UnsafeFunction<T, R> {
        R apply(T from) throws Exception;
    }

    public interface Closable extends AutoCloseable {
        void close();
    }

    public static <T> T safely(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Closable ofClosable(AutoCloseable closeable) {
        return ofRunnable(closeable::close)::run;
    }

    public static <T> Supplier<T> ofSupplier(UnsafeSupplier<T> supplier) {
        return () -> safely(supplier::get);
    }

    public static Runnable ofRunnable(UnsafeRunnable runnable) {
        return () -> safely(() -> { runnable.run(); return null; });
    }

    public static <T, R> Function<T, R> ofFunction(UnsafeFunction<T, R> function) {
        return val -> safely(() -> function.apply(val));
    }

    public static <T> Consumer<T> ofConsumer(UnsafeConsumer<T> consumer) {
        return val -> safely(() -> { consumer.accept(val); return null; });
    }
}
