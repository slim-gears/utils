/**
 *
 */
package com.slimgears.util.generic;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.slimgears.util.generic.ServiceResolvers.defaultResolver;

public class Scope {
    private final static ThreadLocal<ServiceResolver> currentResolver = new ThreadLocal<>();

    public interface Closable extends AutoCloseable {
        void close();
    }

    public static <T> T resolve(Class<T> cls) {
        return current().resolve(cls);
    }

    public static <T> T resolveOrGet(Class<T> cls, Supplier<T> supplier) {
        return Optional.ofNullable(current().resolve(cls)).orElseGet(supplier);
    }

    public static <T> T resolveOrDefault(Class<T> cls, T defaultInstance) {
        return resolveOrGet(cls, () -> defaultInstance);
    }

    public static ServiceResolver current() {
        return Optional
                .ofNullable(currentResolver.get())
                .orElse(defaultResolver);
    }

    public static Closable scope(Consumer<ServiceResolvers.Builder> builder) {
        ServiceResolver prevResolver = current();
        ServiceResolver newResolver = ServiceResolvers
                .builder()
                .parentResolver(prevResolver)
                .apply(builder)
                .build();
        currentResolver.set(newResolver);
        return () -> currentResolver.set(prevResolver);
    }

    public static void withScope(Consumer<ServiceResolvers.Builder> builder, Runnable runnable) {
        Scope.<Void>withScope(builder, () -> { runnable.run(); return null; });
    }

    public static <T> T withScope(Consumer<ServiceResolvers.Builder> builder, Callable<T> callable) {
        try (Closable closable = scope(builder)) {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
