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
    private final static ScopedInstance<ServiceResolver> scopedInstance = new ScopedInstance<>(defaultResolver);

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
        return scopedInstance.current();
    }

    public static ScopedInstance.Closable scope(Consumer<ServiceResolvers.Builder> builder) {
        return scopedInstance.scope(fromBuilder(builder));
    }

    public static void withScope(Consumer<ServiceResolvers.Builder> builder, Runnable runnable) {
        scopedInstance.withScope(fromBuilder(builder), runnable);
    }

    public static <T> T withScope(Consumer<ServiceResolvers.Builder> builder, Callable<T> callable) {
        return scopedInstance.withScope(fromBuilder(builder), callable);
    }

    private static ServiceResolver fromBuilder(Consumer<ServiceResolvers.Builder> builder) {
        return ServiceResolvers
                .builder()
                .parentResolver(current())
                .apply(builder)
                .build();
    }
}
