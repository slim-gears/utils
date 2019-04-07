package com.slimgears.util.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import java.util.HashMap;
import java.util.Map;

public class ThreadSingletonScope implements Scope {
    private final ThreadLocal<Map<Key, Object>> instances = ThreadLocal.withInitial(HashMap::new);

    private ThreadSingletonScope() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> (T)getObjects().computeIfAbsent(key, k -> unscoped.get());
    }

    private Map<Key, Object> getObjects() {
        return instances.get();
    }

    public static void install(Binder binder) {
        ThreadSingletonScope scope = new ThreadSingletonScope();
        binder.bind(ThreadSingletonScope.class).toInstance(scope);
        binder.bindScope(ThreadSingleton.class, scope);
    }
}
