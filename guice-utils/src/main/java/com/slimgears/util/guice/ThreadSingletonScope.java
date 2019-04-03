package com.slimgears.util.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ThreadSingletonScope implements Scope {
    private final ThreadLocal<Map<Key, Object>> instances = new ThreadLocal<>();

    private ThreadSingletonScope() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> (T)getObjects().computeIfAbsent(key, k -> unscoped.get());
    }

    private Map<Key, Object> getObjects() {
        return Optional.ofNullable(instances.get())
                .orElseGet(() -> {
                    Map<Key, Object> map = new HashMap<>();
                    instances.set(map);
                    return map;
                });
    }

    public static void install(Binder binder) {
        ThreadSingletonScope scope = new ThreadSingletonScope();
        binder.bind(ThreadSingletonScope.class).toInstance(scope);
        binder.bindScope(ThreadSingleton.class, scope);
    }
}
